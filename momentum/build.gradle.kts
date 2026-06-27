plugins {
    id("idea")
    id("net.fabricmc.fabric-loom") version "1.17.11"
}

val commonMainOutput = project(":common").extensions.getByType<SourceSetContainer>()["main"].output
val fabricMainOutput = project(":fabric").extensions.getByType<SourceSetContainer>()["main"].output

repositories {
    mavenLocal()
    maven { url = uri("https://maven.fabricmc.net") }
    maven { url = uri("https://maven.terraformersmc.com/") }
    maven { url = uri("https://maven.isxander.dev/releases") }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${rootProject.properties["minecraft_version"]}")

    implementation("net.fabricmc:fabric-loader:${rootProject.properties["fabric_version"]}")
    implementation("net.fabricmc.fabric-api:fabric-api:${rootProject.properties["fabric_api_version"]}")

    compileOnly(commonMainOutput)
    compileOnly(fabricMainOutput)
    runtimeOnly(fabricMainOutput)

    compileOnly("maven.modrinth:yacl:3.9.5+26.2-fabric")
}

sourceSets {
    main {
        java {
            exclude("**/modmenu/MomentumModMenuImpl.java")
        }
    }
}

version = "${rootProject.properties["momentum_version"]}+${rootProject.properties["minecraft_version"]}-fabric"
group = "io.github.milkucha"

base {
    archivesName = "momentum"
}

loom {
    runs {
        named("client") {
            client()
            configName = "Momentum Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            server()
            configName = "Momentum Server"
            ideConfigGenerated(true)
            runDir("run")
        }
    }

    accessWidenerPath = project(":common").loom.accessWidenerPath
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(25)
    }

    processResources {
        inputs.property("version", version)
        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to version))
        }
    }

    withType<Jar> {
        destinationDirectory = rootDir.resolve(project.name).resolve("build").resolve("libs")
    }
}
