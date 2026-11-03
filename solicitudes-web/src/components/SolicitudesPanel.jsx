import { useState } from "react";
import { useSolicitudes } from "../context/SolicitudesContext";

export default function SolicitudesPanel() {
  const { items, cargando, error, crear, resolver, eliminar } = useSolicitudes();
  const [descripcion, setDescripcion] = useState("");
  const [guardando, setGuardando] = useState(false);
  const pendientes = items.filter((item) => item.estado === "PENDIENTE").length;

  async function agregar(evento) {
    evento.preventDefault();
    if (!descripcion.trim() || guardando) return;
    setGuardando(true);
    try {
      const exito = await crear(descripcion.trim());
      if (exito) setDescripcion("");
    } finally {
      setGuardando(false);
    }
  }

  return (
    <main className="container py-4">
      <h1 className="mb-3">Gestión de solicitudes</h1>
      <p>Solicitudes pendientes: <strong>{pendientes}</strong></p>
      <form onSubmit={agregar} className="d-flex gap-2 mb-4">
        <input
          className="form-control"
          aria-label="Descripción de solicitud"
          placeholder="Descripción de solicitud"
          value={descripcion}
          onChange={(e) => setDescripcion(e.target.value)}
        />
        <button className="btn btn-primary" disabled={guardando || !descripcion.trim()}>
          {guardando ? "Guardando..." : "Agregar"}
        </button>
      </form>
      {cargando && <p role="status">Cargando solicitudes...</p>}
      {error && <div role="alert" className="alert alert-danger">{error}</div>}
      {!cargando && items.length === 0 && <p>No hay solicitudes.</p>}
      <ul className="list-group">
        {items.map((item) => (
          <li className="list-group-item d-flex justify-content-between align-items-center gap-2" key={item.id}>
            <span>{item.descripcion} — <strong>{item.estado}</strong></span>
            <div className="d-flex gap-2">
              <button className="btn btn-outline-success btn-sm" disabled={item.estado === "RESUELTA"} onClick={() => resolver(item.id)}>
                Resolver
              </button>
              <button className="btn btn-outline-danger btn-sm" onClick={() => eliminar(item.id)}>
                Eliminar
              </button>
            </div>
          </li>
        ))}
      </ul>
    </main>
  );
}
