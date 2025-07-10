console.log("Starting...");
await cheerpjInit({ enableDebug: true, version: 17})
/*

const cj = await cheerpjRunLibrary("/app/natty/natty-1.1.0-SNAPSHOT.jar:/app/natty/antlr-runtime-3.5.3.jar:/app/natty/slf4j-api-2.0.17.jar:/app/natty/slf4j-nop-2.0.17.jar");
*/

const cj = await cheerpjRunLibrary("/app/slf4j-api-2.0.17.jar:/app/slf4j-nop-2.0.17.jar:/app/antlr-runtime-3.5.3.jar:/app/natty-1.1.0-SNAPSHOT.jar")


const Parser= await cj.org.natty.Parser;
window.nattyParser = await new Parser();

console.log("Parser loaded", window.nattyParser);
