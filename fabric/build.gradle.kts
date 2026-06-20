plugins {
    id("idea")
    id("net.fabricmc.fabric-loom") version "1.17.11"
}

repositories {
    mavenLocal()
    maven { url = uri("https://maven.fabricmc.net") }
    maven { url = uri("https://maven.quiltmc.org/repository/release/") }
    maven { url = uri("https://maven.terraformersmc.com/") }
    maven { url = uri("https://ueaj.dev/maven") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.isxander.dev/releases") }
}

dependencies {
    minecraft("com.mojang:minecraft:${rootProject.properties["minecraft_version"]}")

    implementation("net.fabricmc:fabric-loader:${rootProject.properties["fabric_version"]}")
    implementation("net.fabricmc.fabric-api:fabric-api:${rootProject.properties["fabric_api_version"]}")

    implementation("de.javagl:obj:0.4.0")
    include("de.javagl:obj:0.4.0")

    // Controlify
    compileOnly("dev.isxander:controlify:${rootProject.properties["controlify_version"]}-fabric") {
        isTransitive = false
    }

    implementation(project.project(":common").sourceSets.getByName("main").output)
}

val automobilityLiteClientExcludes = listOf(
    "**/AutomobilityClient.java",
    "**/automobile/render/item/**",
    "**/block/model/**",
    "**/fabric/block/render/**",
    "**/mixin/EntityRenderDispatcherMixin.java",
    "**/mixin/EntityRenderersMixin.java",
    "**/mixin/KeyMappingAccess.java",
    "**/mixin/MultiPlayerGameModeMixin.java",
    "**/mixin/SoundChannelAccess.java",
    "**/mixin/SoundEngineMixin.java",
)

sourceSets {
    main {
        java {
            automobilityLiteClientExcludes.forEach { exclude(it) }
        }
    }
}

loom {
    runs {
        named("client") {
            client()
            configName = "Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            server()
            configName = "Server"
            ideConfigGenerated(true)
            runDir("run")
        }
    }

    accessWidenerPath = project(":common").loom.accessWidenerPath
}

tasks {
    withType<JavaCompile> {
        // include common code in compiled jar
        source(project(":common").sourceSets.main.get().allSource)
        automobilityLiteClientExcludes.forEach { exclude(it) }
    }

    // put all artifacts in the right directory
    withType<Jar> {
        destinationDirectory = rootDir.resolve(project.name).resolve("build").resolve("libs")
    }

    javadoc { source(project(":common").sourceSets.main.get().allJava) }

    processResources {
        val modVersion = rootProject.properties["mod_version"].toString()
        inputs.property("version", modVersion)

        from(project(":common").sourceSets.main.get().resources)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to modVersion))
        }
    }

    named("compileTestJava").configure {
        enabled = false
    }

    named("test").configure {
        enabled = false
    }
}
