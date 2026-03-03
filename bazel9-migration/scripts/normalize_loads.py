import os
import re

def normalize_loads(content):
    # Regex to find load statements for rules_java and rules_proto
    # Supports multiline loads
    patterns = [
        (r'load\("@rules_java//java:defs.bzl", (.*?)\)', "@rules_java//java:defs.bzl"),
        (r'load\("@rules_proto//proto:defs.bzl", (.*?)\)', "@rules_proto//proto:defs.bzl")
    ]
    
    for pattern, repo in patterns:
        matches = list(re.finditer(pattern, content, re.DOTALL))
        # Process in reverse to maintain offsets
        for match in reversed(matches):
            full_match = match.group(0)
            args_str = match.group(1)
            
            # Extract individual arguments (handle both " and ')
            args = re.findall(r'["']([^"']+)["']', args_str)
            
            # Unique sorted args
            unique_args = sorted(list(set(args)))
            
            # Build new load statement
            new_args = ", ".join(f'"{a}"' for a in unique_args)
            new_load = f'load("{repo}", {new_args})'
            
            content = content[:match.start()] + new_load + content[match.end():]
            
    return content

for root, dirs, files in os.walk("."):
    for file in files:
        if file == "BUILD.bazel" or file == "BUILD":
            path = os.path.join(root, file)
            try:
                with open(path, "r") as f:
                    old_content = f.read()
                
                new_content = normalize_loads(old_content)
                
                if old_content != new_content:
                    with open(path, "w") as f:
                        f.write(new_content)
                    print(f"Normalized {path}")
            except Exception as e:
                print(f"Error processing {path}: {e}")
