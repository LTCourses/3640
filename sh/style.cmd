@if not exist ./build.xml if exist ../build.xml cd ..
java -jar lib/test/checkstyle-all.jar -c src/test/checkstyle/checkstyle.xml src/main/java/ src/test/java/
