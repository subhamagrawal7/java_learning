# Java Learning Notes — Day 1: Environment, Mechanics, Maven

## Setup

- JDK: Temurin 25 (LTS) already installed — no need for a newer/different JDK.
- IDE: VS Code + **Extension Pack for Java** (Microsoft) — no need for IntelliJ.
- Maven: installed separately via Homebrew (`brew install maven`) — not bundled with the JDK.

---

## `javac` vs `java` — the compile step

- **Q: Why did a bare `System.out.println(...)` fail with "class expected"?**
  - Java requires *everything* to live inside a class — no loose top-level statements like Python.
- **Two ways to run Java:**
  - `javac Hello.java` → `java Hello` — manual two-step. Produces a `Hello.class` file on disk.
  - `java Hello.java` — single-file source-launch (JDK 11+). Compiles **in memory**, never writes a `.class` file. Always uses the *current* source, every run.
- **Q: If I have `Hello.class` and then edit `Hello.java`, does `java Hello.java` pick up changes?**
  - Yes — it recompiles from source every time, ignores any stale `.class` file.
  - But `java Hello` (running the compiled class directly) does **not** know source changed — it just runs whatever bytecode is in `Hello.class`. Must re-run `javac` to pick up edits.
  - This is why: `.class` = shippable, reusable artifact. Compile is a deliberate step, not automatic — unlike Python interpreting `.py` fresh each run.
- **Q: `java Hello.class` failed with `ClassNotFoundException`?**
  - `java` takes a **class name**, not a filename. Should be `java Hello`, not `java Hello.class`.
- **Bytecode ≠ raw machine code.** It's an intermediate format only the JVM understands — enables "compile once, run anywhere" across OS/CPU as long as a JVM exists there.

---

## `public static void main(String[] args)`

- **`public`** — JVM launcher calls this from outside the class, so it must be externally visible.
- **`static`** — belongs to the class itself, not an instance. JVM hasn't created any object yet at startup, so needs a method callable without one.
- **`void`** — returns nothing to the JVM.
- **`main`** — reserved name the JVM specifically looks for as the entry point.
- **`String[] args`** — command-line arguments (`java Hello foo bar` → `args = ["foo","bar"]`). Must be declared even if unused — it's part of the required signature.
- **Q: Does every file need a `main`?**
  - No — only the entry-point class. Library/helper classes just have regular methods.

### Why my simplified version worked without all of this

- JDK 21 (preview) → JDK 25 (finalized): **implicitly declared classes** + **flexible `main` methods**.
- Writing bare `void main() { ... }` in a file gets auto-wrapped in an invisible class by the compiler — the "everything in a class" rule still holds, just hidden.
- JVM's entry-point search was relaxed to also accept `void main()`, `static void main()` etc., without full `public static void main(String[] args)`.
- **Only works via `java Hello.java` (single-file source-launch).** Real projects / Maven / packaged jars need the full explicit signature — this is a beginner on-ramp feature, not the new norm. Keep writing the full form as habit.

---

## `System.out`

- `System` = built-in class in `java.lang` (auto-imported everywhere).
- `System.out` = a static `PrintStream` field on `System`, wired to standard output.
- Also exists: `System.err` (stderr), `System.in` (stdin).

---

## Maven — why it exists

- Mental model: Python's `pip` + flat scripts → Java needs a fixed folder structure, a compile step, and managed external library (jar) versions. Maven = build tool + dependency manager + project scaffold, all in one.
- **`pom.xml`** — like `requirements.txt` + project config. Declares dependencies, build settings, compiler version.
- **`src/main/java/...`** — standard convention so any tool/IDE/dev knows where source lives.
- Package name **must** map to folder path (e.g. `com.subham.demo` → `com/subham/demo/`) — mandatory in Java, not just convention.

### Commands run, and what each produced

| Command | Produces | Purpose |
| --- | --- | --- |
| `mvn archetype:generate ...` | `hello-maven/` folder: `pom.xml`, `src/main/java`, `src/test/java`, starter `App.java`/`AppTest.java` | Scaffolds a project from a template ("archetype") instead of hand-building structure |
| `mvn compile` | `target/classes/` (compiled `.class` files, mirroring package structure) | Builds source → bytecode, Maven-managed equivalent of running `javac` yourself |
| `java -cp target/classes com.subham.demo.App` | (runs, produces nothing new) | Maven only *builds* by default — doesn't run code. `-cp` = classpath, tells JVM where compiled classes are |
| `mvn test` | `target/test-classes/`, `target/surefire-reports/` | Runs JUnit tests; demonstrates Maven's build **lifecycle** (compile → test, auto-ordered) |
| `mvn package` | `target/hello-maven-1.0-SNAPSHOT.jar` | Bundles compiled classes into one distributable artifact |
| `mvn exec:java -Dexec.mainClass="..."` | (runs the app) | Convenience run command once `exec-maven-plugin` is configured/invoked |

### Archetype version gotcha

- `maven-archetype-quickstart:1.4` (older/common default) hardcodes `maven.compiler.source/target = 7` → fails on modern JDKs with "Source option 7 is no longer supported."
- Fix (manual): add to `pom.xml`:

  ```xml
  <properties>
      <maven.compiler.source>21</maven.compiler.source>
      <maven.compiler.target>21</maven.compiler.target>
  </properties>
  ```

- Better: use `-DarchetypeVersion=1.5` — official archetype with saner defaults (Java 17, JUnit 5.11.0):

    ```bash
    mvn archetype:generate -DgroupId=com.subham.demo -DartifactId=hello-maven \
    -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
    ```

- Takeaway: archetype defaults will always lag behind the installed JDK — knowing *how* to override `<properties>` matters more than finding a version that "never needs touching."

### JDK/Maven version mismatch

- `java --version` and `mvn --version`'s reported Java version can differ — Maven reads `JAVA_HOME` independently of shell `PATH`.
- Fix: set `JAVA_HOME` explicitly (found via `/usr/libexec/java_home -V` on macOS) in `~/.zshrc`, plus prepend `$JAVA_HOME/bin` to `PATH`, then `source ~/.zshrc`.

### Dependency downloads (`Downloading...`)

- Maven Central = public repo of published Java libraries (like PyPI for Java).
- First `mvn compile`/`test` downloads declared dependencies (e.g. JUnit) as `.jar` files.
- Cached locally in `~/.m2/repository/` (organized by `groupId/artifactId/version`) — later runs reuse cache, no re-download.

### VS Code red squiggles on package/JUnit imports

- Not a code error — VS Code's Java language server hadn't "imported" the Maven project yet.
- Fix: open the **project folder** (not a single file) in VS Code with Extension Pack for Java installed; wait for "Importing Java projects" to finish. If stuck: Command Palette → "Java: Clean Java Language Server Workspace."

### `exec-maven-plugin` errors

- Plugin isn't in `pom.xml` by default — must be configured or invoked with `-Dexec.mainClass=...` explicitly.
- Force Maven to retry after fixing: `mvn exec:java -U` (bypasses the cached "not found" result).

### VS Code — what the Java extension gives you once set up

- Install **"Extension Pack for Java"** (Microsoft) — bundles language server, debugger, test runner, Maven support.
- Open the **project folder** (not a single file) — it auto-detects `pom.xml` and resolves the classpath/dependencies (fixes red squiggles on package/JUnit imports).
- Once loaded, it shows a clickable **"Run" / "Debug"** link directly above `public static void main(...)` — no need to manually type `java -cp target/classes com.subham.demo.App` every time.
- Also surfaces a **Maven side panel** (lifecycle goals like `compile`, `test`, `package` clickable from the UI) and a **Java Projects view** for browsing source/dependencies.
- Command Palette → "Java: Clean Java Language Server Workspace" — fixes stale/broken project indexing (e.g. lingering red squiggles after a pom.xml change).
- Command Palette → "Java: Clean Java Language Server Workspace" — fixes stale/broken project indexing (e.g. lingering red squiggles after a pom.xml change).
