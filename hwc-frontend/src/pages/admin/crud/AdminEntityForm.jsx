import { useEffect, useMemo, useState } from "react";
import Button from "../../../components/ui/Button.jsx";
import ImageUpload from "../../../components/ui/ImageUpload.jsx";
import Input from "../../../components/ui/Input.jsx";
import Select from "../../../components/ui/Select.jsx";
import Textarea from "../../../components/ui/Textarea.jsx";

function initialValues(fields, item) {
  return fields.reduce((values, field) => {
    const value = item?.[field.name];
    values[field.name] = value ?? "";
    return values;
  }, {});
}

export default function AdminEntityForm({ config, item, loading, onCancel, onSubmit, options = {} }) {
  const [values, setValues] = useState(() => initialValues(config.fields, item));
  const [errors, setErrors] = useState({});

  useEffect(() => {
    setValues(initialValues(config.fields, item));
    setErrors({});
  }, [config.fields, item]);

  const requiredFields = useMemo(() => config.fields.filter((field) => field.required), [config.fields]);

  function updateValue(name, value) {
    setValues((current) => ({ ...current, [name]: value }));
    setErrors((current) => ({ ...current, [name]: "" }));
  }

  function handleSubmit(event) {
    event.preventDefault();

    const nextErrors = {};
    requiredFields.forEach((field) => {
      if (values[field.name] === "" || values[field.name] === null || values[field.name] === undefined) {
        nextErrors[field.name] = "Ce champ est obligatoire.";
      }
    });

    if (Object.keys(nextErrors).length > 0) {
      setErrors(nextErrors);
      return;
    }

    const payload = config.fields.reduce((data, field) => {
      const rawValue = values[field.name];
      if (field.type === "number" || field.type === "select") {
        data[field.name] = rawValue === "" ? null : Number(rawValue);
      } else {
        data[field.name] = rawValue === "" ? null : rawValue;
      }
      return data;
    }, {});

    onSubmit(payload);
  }

  return (
    <form className="space-y-4" onSubmit={handleSubmit}>
      {config.fields.map((field) => {
        if (field.type === "textarea") {
          return (
            <Textarea
              error={errors[field.name]}
              id={field.name}
              key={field.name}
              label={field.label}
              onChange={(event) => updateValue(field.name, event.target.value)}
              rows={field.rows ?? 4}
              value={values[field.name]}
            />
          );
        }

        if (field.type === "select") {
          const fieldOptions = options[field.name] ?? [];
          return (
            <Select
              error={errors[field.name]}
              id={field.name}
              key={field.name}
              label={field.label}
              onChange={(event) => updateValue(field.name, event.target.value)}
              options={fieldOptions}
              placeholder="Selectionner"
              value={values[field.name]}
            />
          );
        }

        if (field.type === "image") {
          return (
            <ImageUpload
              error={errors[field.name]}
              helperText="Formats acceptes : jpg, png, webp. Taille maximum : 5MB."
              id={field.name}
              key={field.name}
              label={field.label}
              onChange={(value) => updateValue(field.name, value)}
              value={values[field.name]}
            />
          );
        }

        return (
          <Input
            error={errors[field.name]}
            id={field.name}
            key={field.name}
            label={field.label}
            min={field.type === "number" ? 0 : undefined}
            onChange={(event) => updateValue(field.name, event.target.value)}
            type={field.type ?? "text"}
            value={values[field.name]}
          />
        );
      })}

      <div className="flex flex-col-reverse gap-3 pt-2 sm:flex-row sm:justify-end">
        <Button disabled={loading} onClick={onCancel} variant="outline">
          Annuler
        </Button>
        <Button loading={loading} type="submit">
          Enregistrer
        </Button>
      </div>
    </form>
  );
}
