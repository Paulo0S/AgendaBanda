import type { Agenda, Cidade, LocalEvento, LoginResponse, Membro } from "./types";

const API_URL = import.meta.env.VITE_API_URL ?? "/api";

export class ApiError extends Error {}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers ?? {})
    }
  });

  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    throw new ApiError(data.erro ?? "Erro ao comunicar com o servidor.");
  }

  if (response.status === 204) {
    return undefined as T;
  }
  return response.json() as Promise<T>;
}

export const api = {
  login: (login: string, senha: string) => request<LoginResponse>("/auth/login", {
    method: "POST",
    body: JSON.stringify({ login, senha })
  }),
  logout: () => request<{ mensagem: string }>("/auth/logout", { method: "POST" }),
  listarAgenda: () => request<Agenda[]>("/agenda"),
  detalharAgenda: (id: number) => request<Agenda>(`/agenda/${id}`),
  criarAgenda: (data: unknown) => request<Agenda>("/agenda", { method: "POST", body: JSON.stringify(data) }),
  atualizarAgenda: (id: number, data: unknown) => request<Agenda>(`/agenda/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  excluirAgenda: (id: number) => request<{ mensagem: string }>(`/agenda/${id}`, { method: "DELETE" }),
  listarMembros: () => request<Membro[]>("/membros"),
  criarMembro: (data: unknown) => request<Membro>("/membros", { method: "POST", body: JSON.stringify(data) }),
  atualizarMembro: (id: number, data: unknown) => request<Membro>(`/membros/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  alterarStatusMembro: (id: number, ativo: boolean) => request<Membro>(`/membros/${id}/status`, { method: "PATCH", body: JSON.stringify({ ativo }) }),
  excluirMembro: (id: number) => request<{ mensagem: string }>(`/membros/${id}`, { method: "DELETE" }),
  listarCidades: () => request<Cidade[]>("/cidades"),
  listarLocais: () => request<LocalEvento[]>("/locais"),
  criarLocal: (data: unknown) => request<LocalEvento>("/locais", { method: "POST", body: JSON.stringify(data) }),
  atualizarLocal: (id: number, data: unknown) => request<LocalEvento>(`/locais/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  excluirLocal: (id: number) => request<{ mensagem: string }>(`/locais/${id}`, { method: "DELETE" })
};
