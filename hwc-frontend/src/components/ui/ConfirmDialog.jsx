import Button from "./Button.jsx";
import Modal from "./Modal.jsx";

export default function ConfirmDialog({
  cancelLabel = "Annuler",
  confirmLabel = "Confirmer",
  description,
  loading = false,
  onClose,
  onConfirm,
  open,
  title = "Confirmer l'action",
  variant = "danger",
}) {
  return (
    <Modal description={description} onClose={onClose} open={open} title={title}>
      <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button disabled={loading} onClick={onClose} variant="outline">
          {cancelLabel}
        </Button>
        <Button loading={loading} onClick={onConfirm} variant={variant}>
          {confirmLabel}
        </Button>
      </div>
    </Modal>
  );
}
