plugins {
    id("java")
    id("io.freefair.lombok") version "8.11"
    id("checkstyle")
    id("pmd")
    id("com.github.spotbugs") version "6.1.7"
}

group = "ru.ifmo.se"
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("jakarta.platform:jakarta.jakartaee-api:10.0.0")

    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.eclipse.persistence:eclipselink:4.0.4")

    runtimeOnly("org.postgresql:postgresql:42.7.12")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror", "-parameters"))
}

checkstyle {
    toolVersion = "10.21.4"
    isIgnoreFailures = false
    maxWarnings = 0
    maxErrors = 0
    configFile = file("${projectDir}/config/checkstyle/checkstyle.xml")
}

tasks.withType<Checkstyle> {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

pmd {
    toolVersion = "7.11.0"
    isIgnoreFailures = false
    isConsoleOutput = true
    ruleSets = listOf()
    ruleSetFiles = files("${projectDir}/config/pmd/pmd.xml")
}

tasks.withType<Pmd> {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

spotbugs {
    ignoreFailures.set(false)
    effort.set(com.github.spotbugs.snom.Effort.MAX)
    reportLevel.set(com.github.spotbugs.snom.Confidence.LOW)
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
    reports.create("html") {
        required.set(true)
    }
    reports.create("xml") {
        required.set(true)
    }
}

tasks.test {
    useJUnitPlatform()
}
