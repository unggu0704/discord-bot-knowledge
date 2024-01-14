plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.28")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:5.0.0-beta.4")
    implementation ("ch.qos.logback:logback-classic:1.4.1")
    implementation ("org.slf4j:slf4j-api:2.0.3")
    annotationProcessor ("org.projectlombok:lombok:1.18.24")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.5")
}

tasks.jar {
    manifest { attributes["Main-Class"] = "Bot.chatBot.Main" }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


tasks.test {
    useJUnitPlatform()
}