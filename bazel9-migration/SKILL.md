---
name: bazel9-migration
description: Guía y herramientas para migrar un proyecto Java/Proto a Bazel 9. Úsala cuando se te pida actualizar o migrar un workspace a Bazel 9, resolver errores de inicialización de módulos, o cuando falten reglas nativas (java_library, proto_library) al compilar.
---
# Bazel 9 Migration Guide

This skill provides the necessary knowledge and tools to migrate a Bazel 8 project to Bazel 9, specifically focusing on Java and Protobuf projects using Bzlmod.

## Core Migration Issues

When migrating to Bazel 9, you will encounter three main categories of problems:

1. **Native Rules Removed:** Bazel 9 completely removes native Java (`java_library`, `java_binary`, etc.) and Proto (`proto_library`) rules. They must be explicitly loaded in every `BUILD.bazel` file.
2. **Outdated Transitive Dependencies:** Older modules in the Bazel Central Registry (BCR) may still depend on native rules or use deprecated internal APIs (like `JavaInfo` in older `bazel_jar_jar` versions or provider issues in `rules_android`).
3. **Module Resolution Conflicts:** Conflicts may arise when dependencies require different compatibility levels of the same module.

## Workflow

### 1. Update `MODULE.bazel`

Ensure the project uses the latest compatible versions of essential rules. Add or update the following dependencies in `MODULE.bazel` to force newer versions that support Bazel 9. You may need to use `single_version_override` to bypass compatibility level conflicts from transitive dependencies.

```starlark
bazel_dep(name = "rules_java", version = "9.6.1") # Or latest
bazel_dep(name = "rules_proto", version = "7.1.0") # Or latest

# Common problem-makers that need explicit upgrades
bazel_dep(name = "bazel_jar_jar", version = "0.1.14")
bazel_dep(name = "rules_foreign_cc", version = "0.14.0")
bazel_dep(name = "aspect_bazel_lib", version = "2.22.5")
bazel_dep(name = "rules_android", version = "0.7.1")
bazel_dep(name = "grpc-proto", version = "0.0.0-20240627-ec30f58.bcr.1")

single_version_override(
    module_name = "bazel_jar_jar",
    version = "0.1.14",
)
single_version_override(
    module_name = "rules_foreign_cc",
    version = "0.14.0",
)
single_version_override(
    module_name = "aspect_bazel_lib",
    version = "2.22.5",
)
single_version_override(
    module_name = "rules_android",
    version = "0.7.1",
)
```

### 2. Explicitly Load Rules in `BUILD.bazel`

You MUST add explicit `load` statements in every `BUILD` file that uses Java or Proto rules.

**For Java:**
```starlark
load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_test", "java_import")
```
*(Note: If using `java_proto_library`, it also loads from `@rules_java//java:defs.bzl` in newer versions).*

**For Proto:**
```starlark
load("@rules_proto//proto:defs.bzl", "proto_library")
```

**For gRPC Java:**
If you are using `grpc-java`, ensure `java_grpc_library` is loaded correctly:
```starlark
load("@grpc-java//:java_grpc_library.bzl", "java_grpc_library")
```

#### Automation

To quickly add these `load` statements across the entire project, you can use shell commands like:

```bash
# Add base load to files using java rules
find . -name "BUILD.bazel" | xargs grep -lE "java_library|java_binary|java_import|java_test" | xargs grep -L "rules_java" | while read f; do
  sed -i '1i load("@rules_java//java:defs.bzl", "java_library", "java_binary", "java_test", "java_import")' "$f"
done

# Add base load to files using proto rules
find . -name "BUILD.bazel" | xargs grep -l "proto_library" | xargs grep -L "rules_proto" | while read f; do
  sed -i '1i load("@rules_proto//proto:defs.bzl", "proto_library")' "$f"
done
```

**CRITICAL:** After using automated text replacement (like `sed`) to add `load` statements, run the included python script to normalize the files and remove duplicates. Bazel will fail if a symbol is loaded more than once in the same file.

```bash
# Run from the root of the workspace
python3 <path-to-skill>/scripts/normalize_loads.py
```

### 3. Verify and Compile

Run `bazel build //...` to identify any lingering edge cases. Some external dependencies (like older versions of `grpc-proto` brought in transitively) might still complain about missing targets or rules. Address these by explicitly defining the dependency in `MODULE.bazel` to force resolution to a newer version.
