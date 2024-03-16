import net.minecraftforge.gradle.patcher.tasks.ReobfuscateJar
import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import net.minecraftforge.gradle.userdev.UserDevExtension
import net.minecraftforge.gradle.userdev.tasks.JarJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

repositories {
    // First two maven repos host the JEI API. JEI's API is needed for JEI integration.
    maven {
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev")
    }
    // Repository containing Kotlin dependencies
    mavenCentral()
}

buildscript {
    repositories {
        // Repository containing the minecraft forge plugin & MC artifacts
        maven {
            url = uri("https://maven.minecraftforge.net")
        }
    }
    dependencies {
        classpath("net.minecraftforge.gradle", "ForgeGradle", "5.1.+") {
            isChanging = true
        }
        classpath("org.jetbrains.kotlin", "kotlin-gradle-plugin", findProperty("kotlin_version").toString())
    }
}

plugins {
    id("com.github.johnrengelman.shadow").version("7.1.2")
}

apply {
    plugin("net.minecraftforge.gradle")
    plugin("kotlin")
}

version = "${findProperty("mc_version").toString()}-${findProperty("aotd_version").toString()}"
group = "com.davidm1a2.afraidofthedark"
project.setProperty("archivesBaseName", "afraidofthedark")

configure<UserDevExtension> {
    mappings("official", findProperty("mc_version").toString())

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
    val forgeGradle = project.extensions.getByType(DependencyManagementExtension::class.java)

    val mcVersion = findProperty("mc_version").toString()
    val jeiVersion = findProperty("jei_version").toString()
    val forgeVersion = findProperty("forge_version").toString()

    val implementation = configurations["implementation"]
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", findProperty("kotlin_version").toString())
    implementation(forgeGradle.deobf("slimeknights.mantle:Mantle:${mcVersion}-${findProperty("mantle_version").toString()}"))
    implementation(forgeGradle.deobf("slimeknights.tconstruct:TConstruct:${mcVersion}-${findProperty("tinkers_construct_version").toString()}"))

    val shadow = configurations["shadow"]
    shadow("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", findProperty("kotlin_version").toString())

    val minecraft = configurations["minecraft"]
    minecraft("net.minecraftforge", "forge", "$mcVersion-$forgeVersion")

    val compileOnly = configurations["compileOnly"]
    compileOnly(forgeGradle.deobf("mezz.jei:jei-$mcVersion:$jeiVersion:api"))

    val runtimeOnly = configurations["runtimeOnly"]
    runtimeOnly(forgeGradle.deobf("mezz.jei:jei-$mcVersion:$jeiVersion"))
}

tasks {
    withType<Jar> {
        manifest {
            attributes(
                mapOf(
                    "Specification-Title" to "afraidofthedark",
                    "Specification-Vendor" to "afraidofthedarkdavid_m1a2",
                    "Specification-Version" to "1", // We are version 1 of ourselves
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to version,
                    "Implementation-Vendor" to "afraidofthedarkdavid_m1a2",
                    "Implementation-Timestamp" to ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
                )
            )
        }
        finalizedBy("reobfJar")
    }

    withType<JarJar> {
        archiveClassifier.set("")
        enabled = true
        from(provider { project.configurations["shadow"].map(::zipTree).toTypedArray() })
        finalizedBy("reobfJarJar")
    }

    withType<ReobfuscateJar> {
        getByName("jarJar")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
