console.log("Starting...");

await cheerpjInit();

const cj = await cheerpjRunLibrary(
	["/app/natty-1.1.0-SNAPSHOT.jar",
		"/app/antlr-runtime-3.5.3.jar"
	]
)


const Parser = await cj.org.natty.Parser
const obj = await new Parser();

