import org.slf4j.event.Level

plugins {
    id("idea")
    id("net.neoforged.moddev") version "2.0.78"
}

apply(plugin = "net.neoforged.moddev")

dependencies {
    implementation(project.project(":common").sourceSets.getByName("main").output)
}

neoForge {
    version = rootProject.properties["neoforge_version"].toString()

    parchment {
        minecraftVersion = rootProject.properties["minecraft_version"].toString()
        mappingsVersion = rootProject.properties["parchment_release"].toString()
    }

    runs {
        create("Client") {
            client()

        }
        create("Server") {
            server()
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = Level.DEBUG
        }
    }

    mods {
        create(rootProject.properties["archives_base_name"].toString()) {
            sourceSet(sourceSets.main.get())
        }
    }
}

tasks {
    jar {
        val main = project.project(":common").sourceSets.main.get()
        from(main.output.classesDirs)
        from(main.output.resourcesDir)
    }

    named("compileTestJava").configure {
        enabled = false
    }

    val notNeoTask: (Task) -> Boolean = { !it.name.startsWith("neo") && !it.name.startsWith("compileService") }

    withType<JavaCompile>().configureEach {
        source(project(":common").sourceSets.main.get().allSource)
    }

    withType<Javadoc>().matching(notNeoTask).configureEach {
        source(project(":common").sourceSets.main.get().allSource)
    }

    withType<ProcessResources>().matching(notNeoTask).configureEach {
        from(project(":common").sourceSets.main.get().resources)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(rootProject.properties)
        }
    }
}
