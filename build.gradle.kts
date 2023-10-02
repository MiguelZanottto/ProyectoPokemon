plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Lombok para generar código
    implementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
    // Gson para leer y escribir JSON
    implementation("com.google.code.gson:gson:2.10.1")
    // OpenCSV para leer archivos CSV
    implementation("com.opencsv:opencsv:5.8")
    // SQLite para la base de datos
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
    // Ibatis para leer los scripts SQL desde archivos
    implementation("org.mybatis:mybatis:3.5.13")
}

tasks.jar{
    manifest {
        attributes ["Main-class"] = "org.example.Main"
    }

    from ( configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) } )
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
