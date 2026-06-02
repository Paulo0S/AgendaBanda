import { SyntheticEvent, useState } from "react";
import { Box, Button, Card, Heading, Input, Stack, Text } from "@chakra-ui/react";
import { api } from "../api";
import type { Membro } from "../types";

export default function LoginPage({ onLogin }: { onLogin: (usuario: Membro) => void }) {
  const [login, setLogin] = useState("usuario");
  const [senha, setSenha] = useState("usuario");
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function entrar(e: SyntheticEvent) {
    e.preventDefault();
    setErro("");
    setCarregando(true);
    try {
      const resposta = await api.login(login, senha);
      localStorage.setItem("token", resposta.token);
      localStorage.setItem("usuario", JSON.stringify(resposta.usuario));
      onLogin(resposta.usuario);
    } catch (err) {
      setErro(err instanceof Error ? err.message : "Falha no login.");
    } finally {
      setCarregando(false);
    }
  }

  return (
    <Box minH="100vh" display="grid" placeItems="center" bg="bg.subtle" p="6">
      <Card.Root w="100%" maxW="420px" borderRadius="2xl" boxShadow="lg">
        <Card.Body p="8">
          <Stack gap="5" as="form" onSubmit={entrar}>
            <Box>
              <Heading>Agenda da Banda</Heading>
              <Text color="fg.muted">Acesse com login e senha para visualizar a agenda.</Text>
            </Box>
            <Box>
              <Text mb="2">Login</Text>
              <Input value={login} onChange={(e) => setLogin(e.target.value)} />
            </Box>
            <Box>
              <Text mb="2">Senha</Text>
              <Input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} />
            </Box>
            {erro && <Text color="red.500">{erro}</Text>}
            <Button type="submit" loading={carregando}>Entrar</Button>
          </Stack>
        </Card.Body>
      </Card.Root>
    </Box>
  );
}
