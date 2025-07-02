import { Search } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import axios from "axios";

function App() {
  const divScrollDownRef = useRef<null | HTMLDivElement>(null);

  const textareaRef = useRef<HTMLTextAreaElement | null>(null);
  const [inputText, setInputText] = useState("");
  const [prompts, setPrompts] = useState<
    { id: number; value: any; type: string }[]
  >([]);

  useEffect(() => {
    if (divScrollDownRef.current) {
      divScrollDownRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [prompts]);

  const handleInput = () => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = "auto";
      textarea.style.height = `${textarea.scrollHeight}px`;
    }
  };

  const handleSubmit = async () => {
    try {
      if (prompts.length) {
        setPrompts([
          ...prompts,
          {
            id: prompts[prompts.length - 1].id + 1,
            value: inputText,
            type: "prompt",
          },
        ]);
      } else {
        setPrompts([
          {
            id: 1,
            value: inputText,
            type: "prompt",
          },
        ]);
      }

      const response = await axios.post(
        "http://localhost:8080/prompt/queue",
        {
          content: inputText,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      setInputText("");

      console.log("Response:", response.data);
    } catch (error) {
      console.error("Error sending POST request:", error);
      setPrompts(prompts.slice(0, -1));
    }
  };

  useEffect(() => {
    const getAllPrompts = async () => {
      try {
        const prompts = await axios.get("http://localhost:8080/prompt");
        const responses = await axios.get(
          "http://localhost:8080/prompt/response/all"
        );

        let merged: any[] = [];
        prompts.data.forEach((prompt: any) => {
          merged.push({
            id: prompt.id,
            value: prompt.content,
            type: "prompt",
          });

          const response = responses.data[prompt.id];
          if (response) {
            merged.push({
              id: prompt.id,
              value: response,
              type: "response",
            });
          }
        });

        console.log(merged);

        setPrompts(merged);

        console.log("prompts:", prompts.data);
        console.log("responses:", responses.data);
      } catch (error) {
        console.error("Error sending POST request:", error);
      }
    };

    getAllPrompts();
  }, []);

  return (
    <div className="h-screen flex flex-col bg-gray-100">
      {/* Title */}
      <h4 className="text-4xl font-semibold text-center text-gray-800 py-6">
        Closed AI - Model C4
      </h4>

      {/* Response / Prompt History Area */}
      {/* TODO: https://github.com/cure53/DOMPurify */}
      <div className="flex-1 overflow-y-auto px-4">
        <div className="max-w-2xl mx-auto space-y-4 pt-4">
          {prompts.map((prompt: any, idx: number) => (
            <div
              key={idx}
              className="bg-white p-4 rounded shadow whitespace-pre-wrap"
            >
              {prompt.type === "prompt" ? "Prompt:\n" : "Response:\n"}
              <div className="overflow-x-auto">
                <div
                  className="whitespace-pre-wrap rendered-html"
                  dangerouslySetInnerHTML={{ __html: prompt.value }}
                />
              </div>
            </div>
          ))}
        </div>

        <div ref={divScrollDownRef} />
      </div>

      {/* Prompt */}
      <div className="flex items-start justify-center p-4 pb-30">
        <div className="flex items-start gap-2">
          <textarea
            ref={textareaRef}
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            placeholder="Enter text..."
            onInput={handleInput}
            className="bg-white w-150 max-h-60 px-4 py-2 border border-gray-300 rounded-lg shadow-sm resize-none overflow-y-auto focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
          <button
            onClick={handleSubmit}
            className="h-10 p-2 mt-1 rounded-md bg-gray-200 hover:bg-gray-300 text-gray-700 hover:text-black transition-colors"
            aria-label="Search"
          >
            <Search className="w-5 h-5" />
          </button>
        </div>
      </div>
    </div>
  );
}

export default App;
