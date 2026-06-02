import { useEffect, useState } from "react";
import { Box, Button, Flex, Heading, HStack, Text } from "@chakra-ui/react";
import { api } from "./api";
import type { Membro } from "./types";
import LoginPage from "./components/LoginPage";
import AgendaPage from "./components/AgendaPage";
import UsersPage from "./components/UsersPage";
import LocaisPage from "./components/LocaisPage";
import { ColorModeButton } from "./components/ui/ColorModeButton";

type Aba = "agenda" | "usuarios" | "locais";

export default function App() {
  const [usuario, setUsuario] = useState<Membro | null>(null);
  const [aba, setAba] = useState<Aba>("agenda");

  useEffect(() => {
    const salvo = localStorage.getItem("usuario");
    if (salvo) setUsuario(JSON.parse(salvo));
  }, []);

  async function sair() {
    await api.logout().catch(() => undefined);
    localStorage.removeItem("token");
    localStorage.removeItem("usuario");
    setUsuario(null);
  }

  if (!usuario) {
    return <LoginPage onLogin={setUsuario} />;
  }

  return (
    <Box minH="100vh" bg="bg.subtle">
      <Box borderBottomWidth="1px" bg="bg.panel" position="sticky" top="0" zIndex="10">
        <Flex maxW="1180px" mx="auto" px="6" py="4" justify="space-between" align="center" gap="4" wrap="wrap">
          <Box>
            <Heading size="lg">Agenda da Banda</Heading>
            <Text color="fg.muted">Logado como {usuario.nome}</Text>
          </Box>
          <HStack wrap="wrap">
            <Button variant={aba === "agenda" ? "solid" : "outline"} onClick={() => setAba("agenda")}>Agenda</Button>
            {usuario.administrador && <Button variant={aba === "usuarios" ? "solid" : "outline"} onClick={() => setAba("usuarios")}>Usuários</Button>}
            {usuario.administrador && <Button variant={aba === "locais" ? "solid" : "outline"} onClick={() => setAba("locais")}>Locais</Button>}
            <ColorModeButton />
            <Button variant="ghost" onClick={sair}>Sair</Button>
          </HStack>
        </Flex>
      </Box>

      <Box maxW="1180px" mx="auto" p="6">
        {aba === "agenda" && <AgendaPage usuario={usuario} />}
        {aba === "usuarios" && usuario.administrador && <UsersPage />}
        {aba === "locais" && usuario.administrador && <LocaisPage />}
      </Box>
    </Box>
  );
}
