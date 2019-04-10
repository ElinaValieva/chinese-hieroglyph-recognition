FROM openjdk:8
ADD target/hieroglyph_recognition_backend.jar hieroglyph_recognition_backend.jar
CMD ["java", "-jar", "hieroglyph_recognition_backend.jar"]