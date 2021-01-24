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
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("net.minecraftforge.gradle", "ForgeGradle", "3.+") {
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

configurations["compile"].extendsFrom(configurations.create("shade"))

version = "1.14.4-1.5.0"
group = "com.davidm1a2.afraidofthedark"
project.setProperty("archivesBaseName", "afraidofthedark")

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

configure<UserDevExtension> {
    mappings("snapshot", "20190719-1.14.3")

    runs.create("client") {
        workingDirectory(project.file("run"))
        property("forge.logging.markers", "COREMOD,CORE,LOADING,CONFIG") // eg: SCAN,REGISTRIES,REGISTRYDUMP
        property("forge.logging.console.level", "info")
        mods.create("afraidofthedark") {
            source(the<JavaPluginConvention>().sourceSets.getByName("main"))
        }
    }

    runs.create("server") {
        workingDirectory(project.file("run"))
        property("forge.logging.markers", "COREMOD,CORE,LOADING,CONFIG") // eg: SCAN,REGISTRIES,REGISTRYDUMP
        property("forge.logging.console.level", "info")
        mods.create("afraidofthedark") {
            source(the<JavaPluginConvention>().sourceSets.getByName("main"))
        }
    }
}

dependencies {
    val implementation = configurations["implementation"]
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", findProperty("kotlin_version").toString())

    val shade = configurations["shade"]
    shade("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", findProperty("kotlin_version").toString())

    val minecraft = configurations["minecraft"]
    minecraft("net.minecraftforge", "forge", "1.14.4-28.2.23")
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