# CLAUDE.md

Este archivo proporciona orientación a Claude Code (claude.ai/code) cuando trabaja con el código de este repositorio.

## Objetivo del Proyecto

Entorno experimental de I+D para Google Gemini / Vertex AI, integrado con frameworks modernos de Java. El código puede sufrir refactorizaciones drásticas — prioriza la exploración y el aprendizaje sobre la estabilidad a largo plazo en las capas `example/`.

## Sistema de Build

**Bazel 8.x (Bzlmod)** — toda la gestión de dependencias está centralizada en `MODULE.bazel`.

```bash
# Compilar todo
bazel build //...

# Ejecutar un target específico
bazel run //java/example/helidon/grpc/server:helidon-grpc-server

# Construir/publicar imagen OCI para despliegue
bazel run //java/product/iatevaleagent:iatevaleagent_gcloud_push
```

**Importante:** NO ejecutar comandos de Bazel de forma proactiva para validar cambios. Claude Code e IntelliJ utilizan cachés separadas — ejecutar Bazel desde aquí obliga a reconstruir todo el proyecto en el IDE. Solo ejecutar Bazel bajo petición explícita del usuario.

## Configuración del Entorno

Crear `~/.iatevale/config.properties`:
```properties
project.id=your-gcloud-project-id
credentials=/home/{username}/.iatevale/your-credential-file.json
```

Roles IAM requeridos: `Storage Admin`, `Cloud Datastore User`, `Vertex AI User`.

Para ejemplos de Telegram: configurar la variable de entorno `TELEGRAM_BOT_TOKEN`.

## Arquitectura

El proyecto está organizado en capas de dependencia (las capas inferiores no pueden depender de las superiores):

| Capa | Ubicación | Propósito |
|---|---|---|
| Bootstrap | `/java/bootstrap` | Utilidades base: helpers Gson/Protobuf, contexto GCloud, sistema `Unit` |
| A2A | `/java/a2a` | Comunicación Agente a Agente: modelos, motores de limpieza, despacho, repositorios |
| Platform | `/java/platform` | Frameworks de agentes reutilizables y servidores de despacho (ianews, laliga) |
| Product | `/java/product` | Aplicaciones de producción (iatevaleagent) — contenerizadas vía OCI |
| Example | `/java/example` | Más de 30 ejemplos cookbook: AI, Helidon SE, Xodus, Javelit UI, Telegram |
| Lib | `/java/lib` | Utilidades compartidas en todo el proyecto |
| Third Party | `/java/third_party` | Wrappers/adaptaciones de SDKs externos (GenAI, Closeable, Interactions) |
| Proto | `/proto` | Definiciones Protobuf para servicios gRPC y modelos de dominio |

### Tecnologías Clave

- **IA:** Google GenAI SDK (Gemini), Vertex AI SDK, Agent Development Kit (ADK)
- **Web/Async:** Helidon SE 4 con virtual threads — **nunca Helidon MP/MicroProfile**
- **DI:** Helidon Service Registry (programático, no basado en anotaciones)
- **Persistencia:** JetBrains Xodus (KV/Entity/VFS embebido) + Google Cloud Datastore
- **RPC:** gRPC + Protocol Buffers
- **UI:** Javelit para prototipado de UI web en Java
- **Mensajería:** Telegram Bots SDK (Long Polling)
- **Contenedores:** Imágenes OCI construidas y publicadas con `rules_oci` de Bazel

### Mandatos de Diseño

- **Java 21 mínimo** — usar virtual threads y características modernas del lenguaje
- **Solo Helidon SE** — sin MicroProfile
- **Helidon Service Registry** para DI — no usar Dagger ni Guice en código nuevo
- **JSpecify** (`org.jspecify.annotations`) para anotaciones de nulabilidad
- **Sistema `Unit`** — usarlo para identificar componentes, gestionar entornos y configurar el logging de forma centralizada
- **Wrappers de GenAI** — preferir las abstracciones en `/java/third_party` antes que llamar directamente a los SDKs
- Los nuevos experimentos van en paquetes Bazel aislados bajo `/java/example/`

## Documentación Actualizada de Librerías

Usar la herramienta **Context7** (`mcp__claude_ai_Context7__resolve-library-id` + `mcp__claude_ai_Context7__query-docs`) para obtener documentación actualizada de cualquier librería o API de Java antes de escribir código o responder preguntas sobre su uso. Esto aplica especialmente a:

- Helidon SE 4 (reactive, routing, gRPC, Service Registry)
- Google GenAI SDK / Vertex AI SDK / ADK
- Bazel / rules_oci / Bzlmod
- JetBrains Xodus
- Java 21+ (virtual threads, records, sealed classes, pattern matching)
- Protocol Buffers / gRPC Java

## Despliegue en Google Cloud Run

```bash
# 1. Configurar autenticación Docker
gcloud auth configure-docker europe-west4-docker.pkg.dev

# 2. Construir y publicar (actualizar antes el atributo repository en BUILD.bazel)
bazel run //java/product/iatevaleagent:iatevaleagent_gcloud_push

# 3. Desplegar (usar el digest sha256 impreso por el comando anterior)
gcloud run jobs create iatevaleagent-job \
    --image=europe-west4-docker.pkg.dev/project-id/repo-name/iatevaleagent@sha256:DIGEST \
    --project=project-id
gcloud run jobs execute iatevaleagent-job
```
