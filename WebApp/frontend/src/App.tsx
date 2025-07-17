import { useState } from "react";
import sendPrompt from "./utils/sendPrompt";
import { createSSEConnection } from "./utils/sse";

type ChatMessage = {
  type: "prompt" | "response";
  content: string;
};

function App() {
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState<ChatMessage[]>([]);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key == "Enter" && !e.shiftKey) {
      e.preventDefault(); // Prevent new line
      handleSubmit();
    }
  };

  const handleSubmit = async () => {
    if (message.trim() === "") {
      console.error("Can't send an empty prompt.");
      return;
    }

    console.log("Sending:", message);

    const result = await sendPrompt("default", message);

    if (!result) {
      console.error("An error occurred during response from the server.");
      return;
    }

    setMessages((prev) => [...prev, { type: "prompt", content: message }]);
    setMessage("");
    handleStartConnection();
  };

  const handleStartConnection = () => {
    createSSEConnection({
      url: "http://localhost:8080/get-response",
      withCredentials: true,

      onMessage: (event) => {
        if (event.data === "Connection Established...") {
          return;
        }

        if (typeof event.data !== "string") {
          console.error("Expected a string from SSE, got:", typeof event.data);
          return;
        }

        setMessages((prev) => [
          ...prev,
          { type: "response", content: event.data },
        ]);

        console.log(`Received response: ${event.data}`);
      },

      eventName: "response",

      onOpen: () => console.log("Connection established"),
      onError: (error) => console.error("onerror", error),
      onClose: () => console.log("Connection closed by server."),
    });
  };

  // TODO: Should show user if an important error has happend

  return (
    <div>
      <div className="bg-gray-200">
        <div className="flex flex-col h-screen">
          {/* Title */}
          <h1 className="text-4xl text-gray-800 py-10 text-center">
            ClosedAI - Model C4
          </h1>

          {/* Chat History */}
          <div className="flex-4 flex justify-center">
            <div className="bg-white flex justify-center w-6xl h-full rounded-xl">
              <ul>
                {messages.map((message, i) => (
                  <li key={i}>{message.content}</li>
                ))}
              </ul>
            </div>
          </div>

          {/* Input field */}
          <div className="flex-1 flex justify-center">
            <div className="h-full w-2xl flex justify-center items-center">
              <textarea
                onChange={(e) => setMessage(e.target.value)}
                onKeyDown={handleKeyDown}
                value={message}
                required
                placeholder="Prompt..."
                className="bg-white h-4/5 w-2xl rounded-md p-2 resize-none text-lg"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
