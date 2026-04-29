import { ImagePlus, X } from "lucide-react";
import { useRef, useState } from "react";
import toast from "react-hot-toast";
import { uploadImage } from "../../api/uploadApi.js";
import Button from "./Button.jsx";
import Input from "./Input.jsx";
import { cn } from "../../utils/cn.js";

const ACCEPTED_TYPES = ["image/jpeg", "image/png", "image/webp"];
const MAX_SIZE_BYTES = 5 * 1024 * 1024;

export default function ImageUpload({ error, helperText, id, label, onChange, value }) {
  const inputRef = useRef(null);
  const [loading, setLoading] = useState(false);

  async function handleFileChange(event) {
    const file = event.target.files?.[0];
    event.target.value = "";

    if (!file) {
      return;
    }

    if (!ACCEPTED_TYPES.includes(file.type)) {
      toast.error("Format autorise : jpg, png ou webp.");
      return;
    }

    if (file.size > MAX_SIZE_BYTES) {
      toast.error("Le fichier ne doit pas depasser 5MB.");
      return;
    }

    setLoading(true);
    try {
      const uploaded = await uploadImage(file);
      onChange(uploaded.url);
      toast.success("Image uploadee avec succes.");
    } catch (uploadError) {
      const message = uploadError.response?.data?.message ?? uploadError.response?.data?.error ?? "Upload impossible.";
      toast.error(message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="space-y-3">
      <Input
        error={error}
        helperText={helperText}
        id={id}
        label={label}
        onChange={(event) => onChange(event.target.value)}
        placeholder="https://..."
        type="url"
        value={value}
      />

      <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
        <input
          accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp"
          className="hidden"
          onChange={handleFileChange}
          ref={inputRef}
          type="file"
        />
        <Button loading={loading} onClick={() => inputRef.current?.click()} variant="outline">
          <ImagePlus className="h-4 w-4" />
          Uploader une image
        </Button>
        {value ? (
          <Button disabled={loading} onClick={() => onChange("")} variant="ghost">
            <X className="h-4 w-4" />
            Retirer
          </Button>
        ) : null}
      </div>

      {value ? (
        <div className={cn("flex h-28 w-full items-center justify-center rounded-lg border border-border bg-muted/40 p-3")}>
          <img alt="" className="max-h-full max-w-full object-contain" src={value} />
        </div>
      ) : null}
    </div>
  );
}
