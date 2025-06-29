import { Search } from "lucide-react";
import { useRef } from "react";

function App() {
  const textareaRef = useRef<HTMLTextAreaElement | null>(null);

  const handleInput = () => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = "auto";
      textarea.style.height = `${textarea.scrollHeight}px`;
    }
  };

  return (
    <>
      <div className="h-screen flex flex-col bg-gray-100">
        {/* Title */}
        <h4 className="text-4xl font-semibold text-center text-gray-800 py-6">
          Closed AI - Model C4
        </h4>

        {/* Response Area */}
        <div className="flex-1 overflow-y-auto px-4">
          <div className="max-w-2xl mx-auto space-y-4">
            <div className="bg-white p-4 rounded shadow whitespace-pre-wrap">
              {`This is a long response that goes over multiple lines.

It preserves line breaks.

Even if the text gets very long, it will wrap nicely.`}
            </div>

            <div className="bg-white p-4 rounded shadow whitespace-pre-wrap">
              {`Another response here.

- With bullet points
- And line breaks
- And more text just to make sure it scrolls nicely when it gets really tall.`}
            </div>
          </div>
        </div>

        {/* Prompt */}
        <div className="flex items-start justify-center p-4 pb-30">
          <div className="flex items-start gap-2">
            <textarea
              ref={textareaRef}
              placeholder="Enter text..."
              onInput={handleInput}
              className="bg-white w-150 max-h-60 px-4 py-2 border border-gray-300 rounded-lg shadow-sm resize-none overflow-y-auto focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
            <button
              className="h-10 p-2 mt-1 rounded-md bg-gray-200 hover:bg-gray-300 text-gray-700 hover:text-black transition-colors"
              aria-label="Search"
            >
              <Search className="w-5 h-5" />
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default App;
