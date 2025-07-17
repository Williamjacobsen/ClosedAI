import { useState } from "react";

function App() {
  const [message, setMessage] = useState("");

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key == "Enter" && !e.shiftKey) {
      e.preventDefault(); // Prevent new line
      handleSubmit();
    }
  };

  const handleSubmit = () => {
    if (message.trim() !== "") {
      console.log("Sending:", message);
      setMessage("");
    }
  };

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
            <div className="bg-white flex justify-center w-6xl h-full rounded-xl"></div>
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
