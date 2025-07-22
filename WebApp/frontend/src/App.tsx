import { useRef, useState } from "react";
import type { ChatMessage } from "./types/chatMessage";
import handleSubmit from "./hooks/useSendPrompt";

function App() {
  const [prompt, setPrompt] = useState<string>("");
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const sendingRef = useRef<boolean>(false);

  const handleKeyDown = async (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key == "Enter" && !e.shiftKey) {
      e.preventDefault(); // Prevent new line

      if (!sendingRef.current) {
        sendingRef.current = true;
        await handleSubmit(prompt, setPrompt, setMessages, sendingRef);
      }
    }
  };

  // TODO: Should show user if an important error has happend

  return (
    <div className="h-screen">
      <div className="bg-gray-100">
        <div className="flex flex-col h-screen">
          {/* Title */}
          <h1 className="text-4xl text-gray-800 py-10 text-center">
            ClosedAI - Model C4
          </h1>

          {/* Chat History */}
          <div className="flex-4 flex justify-center overflow-y-auto">
            <div className="bg-white flex justify-center w-6xl h-full rounded-xl overflow-y-auto whitespace-pre-wrap">
              <ul className="w-6xl px-10">
                {messages.map((message, i) => (
                  <li
                    key={i}
                    className={`my-2 flex ${
                      message.type === "prompt"
                        ? "justify-end"
                        : "justify-start"
                    }`}
                  >
                    <div
                      className={`p-3 rounded-lg max-w-4xl overflow-x-auto ${
                        message.type === "prompt"
                          ? "bg-gray-900 text-white rounded-br-none"
                          : "bg-gray-100 text-black rounded-bl-none"
                      }`}
                    >
                      {message.content}
                    </div>
                  </li>
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
