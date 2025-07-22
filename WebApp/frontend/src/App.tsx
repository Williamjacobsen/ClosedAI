import { useState } from "react";
import type { ChatMessage } from "./types/chatMessage";
import handleSubmit from "./hooks/useSendPrompt";

function App() {
  const [prompt, setPrompt] = useState("");
  const [messages, setMessages] = useState<ChatMessage[]>([]);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key == "Enter" && !e.shiftKey) {
      e.preventDefault(); // Prevent new line
      handleSubmit(prompt, setPrompt, setMessages);
    }
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
                onChange={(e) => setPrompt(e.target.value)}
                onKeyDown={handleKeyDown}
                value={prompt}
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
