import { SyntheticEvent, useEffect, useState } from "react";
import { Box, Button, Card, Grid, HStack, Input, Stack, Text } from "@chakra-ui/react";
import { api } from "../api";
import type { Cidade, LocalEvento } from "../types";

const vazio = { nome: "", endereco: "", complemento: "", cidadeId: "" };

export default function LocaisPage() {
  const [locais, setLocais] = useState<LocalEvento[]>([]);
  const [cidades, setCidades] = useState<Cidade[]>([]);
  const [form, setForm] = useState({ ...vazio });
  const [editando, setEditando] = useState<number | null>(null);
  const [erro, setErro] = useState("");

  async function carregar() {
    const [l, c] = await Promise.all([api.listarLocais(), api.listarCidades()]);
    setLocais(l);
    setCidades(c);
  }
  useEffect(() => { carregar().catch((e) => setErro(e.message)); }, []);

  async function salvar(e: SyntheticEvent) {
    e.preventDefault();
    const payload = { ...form, cidadeId: Number(form.cidadeId) };
    try {
      if (editando) await api.atualizarLocal(editando, payload);
      else await api.criarLocal(payload);
      setForm({ ...vazio });
      setEditando(null);
      await carregar();
    } catch (err) {
      setErro(err instanceof Error ? err.message : "Erro ao salvar local.");
    }
  }

  return (
    <Stack gap="5">
      <Box><Text fontSize="2xl" fontWeight="bold">Locais</Text><Text color="fg.muted">Cadastro de locais usados nos eventos.</Text></Box>
      {erro && <Text color="red.500">{erro}</Text>}
      <Card.Root borderRadius="2xl"><Card.Body><Stack as="form" gap="4" onSubmit={salvar}><Grid templateColumns={{ base: "1fr", md: "repeat(4, 1fr)" }} gap="4"><Box><Text mb="1">Nome</Text><Input value={form.nome} onChange={(e) => setForm({ ...form, nome: e.target.value })} required /></Box><Box><Text mb="1">Endereço</Text><Input value={form.endereco} onChange={(e) => setForm({ ...form, endereco: e.target.value })} required /></Box><Box><Text mb="1">Complemento</Text><Input value={form.complemento} onChange={(e) => setForm({ ...form, complemento: e.target.value })} /></Box><Box><Text mb="1">Cidade</Text><select className="native-field" value={form.cidadeId} onChange={(e) => setForm({ ...form, cidadeId: e.target.value })} required><option value="">Selecione</option>{cidades.map((c) => <option key={c.codigoCidade} value={c.codigoCidade}>{c.nome}/{c.uf}</option>)}</select></Box></Grid><HStack><Button type="submit">Salvar</Button><Button type="button" variant="outline" onClick={() => { setForm({ ...vazio }); setEditando(null); }}>Limpar</Button></HStack></Stack></Card.Body></Card.Root>
      <Grid templateColumns={{ base: "1fr", md: "repeat(2, 1fr)" }} gap="3">{locais.map((l) => <Card.Root key={l.codigoLocal} borderRadius="2xl"><Card.Body><Stack><Text fontWeight="bold">{l.nome}</Text><Text>{l.endereco} {l.complemento}</Text><Text>{l.cidadeNome}/{l.uf}</Text><Button size="sm" onClick={() => { setEditando(l.codigoLocal); setForm({ nome: l.nome, endereco: l.endereco, complemento: l.complemento ?? "", cidadeId: String(l.cidadeId) }); }}>Editar</Button></Stack></Card.Body></Card.Root>)}</Grid>
    </Stack>
  );
}
