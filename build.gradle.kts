import net.minecraftforge.gradle.userdev.UserDevExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        maven {
            url = uri("https://files.minecraftforge.net/maven")
        }
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        mavenCentral()
    }
    dependencies {
        classpath("net.minecraftforge.gradle", "ForgeGradle", "5.1.+") {
            isChanging = true
        }
        classpath("org.jetbrains.kotlin", "kotlin-gradle-plugin", findProperty("kotlin_version").toString())
    }
}

apply {
    plugin("net.minecraftforge.gradle")
    plugin("eclipse")
    plugin("maven-publish")
    plugin("kotlin")
}

configurations["implementation"].extendsFrom(configurations.create("shade"))

version = "1.16.5-1.7.0"
group = "com.davidm1a2.afraidofthedark"
project.setProperty("archivesBaseName", "afraidofthedark")

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<UserDevExtension> {
    mappings("official", "1.16.5")

    runs.create("client") {
        workingDirectory(project.file("run"))
        property("forge.logging.markers", "COREMOD,CORE,LOADING,CONFIG") // eg: SCAN,REGISTRIES,REGISTRYDUMP
        property("forge.logging.console.level", "info")
        mods.create("afraidofthedark") {
            source(the<JavaPluginExtension>().sourceSets.getByName("main"))
        }
    }

    runs.create("server") {
        workingDirectory(project.file("run"))
        property("forge.logging.markers", "COREMOD,CORE,LOADING,CONFIG") // eg: SCAN,REGISTRIES,REGISTRYDUMP
        property("forge.logging.console.level", "info")
        mods.create("afraidofthedark") {
            source(the<JavaPluginExtension>().sourceSets.getByName("main"))
        }
    }
}

dependencies {
    val implementation = configurations["implementation"]
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", findProperty("kotlin_version").toString())

    val shade = configurations["shade"]
    shade("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", findProperty("kotlin_version").toString())

    val minecraft = configurations["minecraft"]
    minecraft("net.minecraftforge", "forge", "1.16.5-36.2.0")
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to "afraidofthedark",
                "Specification-Vendor" to "afraidofthedarkdavid_m1a2",
                "Specification-Version" to "1", // We are version 1 of ourselves
                "Implementation-Title" to project.name,
                "Implementation-Version" to version,
                "Implementation-Vendor" to "afraidofthedarkdavid_m1a2",
                "Implementation-Timestamp" to ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
            )
        )
    }
    configurations.getByName("shade").onEach {
        from(project.zipTree(it)) {
            exclude("META-INF", "META-INF/**")
        }
    }
}
