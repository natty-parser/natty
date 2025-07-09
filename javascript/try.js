console.log("Starting...");
await cheerpjInit()

const cj = await cheerpjRunLibrary("/app/natty/natty-1.1.0-SNAPSHOT.jar:/app/natty/antlr-runtime-3.5.3.jar:/app/natty/slf4j-api-2.0.17.jar:/app/natty/slf4j-nop-2.0.17.jar");

console.log("hoi")



const Parser= await cj.org.natty.Parser;
console.log("Parser loaded");
window.nattyParser = Parser();

