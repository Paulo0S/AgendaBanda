import { SyntheticEvent, useEffect, useState } from "react";
import { Box, Button, Card, Grid, HStack, Input, Stack, Text } from "@chakra-ui/react";
import { api } from "../api";
import type { Membro } from "../types";

const vazio = { nome: "", cpf: "", email: "", login: "", senha: "", administrador: false, ativo: true, perfil: "MEMBRO", instrumento: "", biografia: "", especialidade: "" };

export default function UsersPage() {
  const [membros, setMembros] = useState<Membro[]>([]);
  const [form, setForm] = useState({ ...vazio });
  const [editando, setEditando] = useState<number | null>(null);
  const [erro, setErro] = useState("");

  async function carregar() {
    setMembros(await api.listarMembros());
  }

  useEffect(() => { carregar().catch((e) => setErro(e.message)); }, []);

  function preencher(m: Membro) {
    setEditando(m.codigo);
    setForm({
      nome: m.nome, cpf: m.cpf, email: m.email, login: m.login, senha: "", administrador: m.administrador,
      ativo: m.ativo, perfil: m.perfil, instrumento: m.instrumento ?? "", biografia: m.biografia ?? "", especialidade: m.especialidade ?? ""
    });
  }

  async function salvar(e: SyntheticEvent) {
    e.preventDefault();
    setErro("");
    try {
      if (editando) await api.atualizarMembro(editando, form);
      else await api.criarMembro(form);
      setForm({ ...vazio });
      setEditando(null);
      await carregar();
    } catch (err) {
      setErro(err instanceof Error ? err.message : "Erro ao salvar usuário.");
    }
  }

  function update(campo: string, valor: string | boolean) {
    setForm((prev) => ({ ...prev, [campo]: valor }));
  }

  return (
    <Stack gap="5">
      <Box><Text fontSize="2xl" fontWeight="bold">Gerenciamento de usuários</Text><Text color="fg.muted">Apenas administradores acessam esta tela.</Text></Box>
      {erro && <Text color="red.500">{erro}</Text>}
      <Card.Root borderRadius="2xl"><Card.Body>
        <Stack as="form" gap="4" onSubmit={salvar}>
          <Grid templateColumns={{ base: "1fr", md: "repeat(3, 1fr)" }} gap="4">
            <Box><Text mb="1">Nome</Text><Input value={form.nome} onChange={(e) => update("nome", e.target.value)} required /></Box>
            <Box><Text mb="1">CPF</Text><Input value={form.cpf} onChange={(e) => update("cpf", e.target.value)} required /></Box>
            <Box><Text mb="1">E-mail</Text><Input type="email" value={form.email} onChange={(e) => update("email", e.target.value)} required /></Box>
            <Box><Text mb="1">Login</Text><Input value={form.login} onChange={(e) => update("login", e.target.value)} required /></Box>
            <Box><Text mb="1">Senha {editando && "(opcional)"}</Text><Input type="password" value={form.senha} onChange={(e) => update("senha", e.target.value)} /></Box>
            <Box><Text mb="1">Perfil</Text><select className="native-field" value={form.perfil} onChange={(e) => update("perfil", e.target.value)}><option value="MEMBRO">Membro</option><option value="MUSICO">Músico</option><option value="PRODUTOR">Produtor</option></select></Box>
            {form.perfil === "MUSICO" && <Box><Text mb="1">Instrumento</Text><Input value={form.instrumento} onChange={(e) => update("instrumento", e.target.value)} /></Box>}
            {form.perfil === "PRODUTOR" && <Box><Text mb="1">Especialidade</Text><Input value={form.especialidade} onChange={(e) => update("especialidade", e.target.value)} /></Box>}
          </Grid>
          <HStack><label><input type="checkbox" checked={form.administrador} onChange={(e) => update("administrador", e.target.checked)} /> Administrador</label><label><input type="checkbox" checked={form.ativo} onChange={(e) => update("ativo", e.target.checked)} /> Ativo</label></HStack>
          <HStack><Button type="submit">{editando ? "Atualizar" : "Cadastrar"}</Button><Button type="button" variant="outline" onClick={() => { setEditando(null); setForm({ ...vazio }); }}>Limpar</Button></HStack>
        </Stack>
      </Card.Body></Card.Root>
      <Grid templateColumns={{ base: "1fr", md: "repeat(2, 1fr)" }} gap="3">
        {membros.map((m) => <Card.Root key={m.codigo} borderRadius="2xl"><Card.Body><Stack gap="2"><Text fontWeight="bold">{m.nome}</Text><Text>{m.login} • {m.email}</Text><Text>{m.perfil} {m.administrador ? "• Admin" : ""} • {m.ativo ? "Ativo" : "Inativo"}</Text><HStack><Button size="sm" onClick={() => preencher(m)}>Editar</Button><Button size="sm" variant="outline" onClick={() => api.alterarStatusMembro(m.codigo, !m.ativo).then(carregar)}>{m.ativo ? "Desativar" : "Ativar"}</Button></HStack></Stack></Card.Body></Card.Root>)}
      </Grid>
    </Stack>
  );
}
