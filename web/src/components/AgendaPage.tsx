import { useEffect, useMemo, useState } from "react";
import { Badge, Box, Button, Flex, Grid, Heading, HStack, Stack, Text } from "@chakra-ui/react";
import { api } from "../api";
import type { Agenda, Cidade, LocalEvento, Membro } from "../types";
import EventForm from "./EventForm";

const tiposEvento: Record<Agenda["tipoEspecifico"], { nome: string; cor: string }> = {
  ENSAIO: { nome: "Ensaio", cor: "#3182CE" },
  SHOW: { nome: "Show", cor: "#E53E3E" },
  REUNIAO: { nome: "Reunião", cor: "#805AD5" },
  EVENTO: { nome: "Evento", cor: "#4A5568" }
};

const diasSemana = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

function dataEvento(valor: string) {
  return new Date(valor);
}

function chaveData(data: Date) {
  const ano = data.getFullYear();
  const mes = String(data.getMonth() + 1).padStart(2, "0");
  const dia = String(data.getDate()).padStart(2, "0");
  return `${ano}-${mes}-${dia}`;
}

function nomeMes(data: Date) {
  const texto = data.toLocaleDateString("pt-BR", { month: "long", year: "numeric" });
  return texto.charAt(0).toUpperCase() + texto.slice(1);
}

function horario(valor: string) {
  return dataEvento(valor).toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
}

function dataCompleta(valor: string) {
  return dataEvento(valor).toLocaleString("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit"
  });
}

function ModalShell({ children, onClose }: { children: React.ReactNode; onClose: () => void }) {
  return (
    <Box
      position="fixed"
      inset="0"
      bg="blackAlpha.600"
      zIndex="modal"
      display="flex"
      alignItems="center"
      justifyContent="center"
      p="4"
      onClick={onClose}
    >
      <Box w="100%" maxW="760px" maxH="calc(100vh - 32px)" overflowY="auto" onClick={(e) => e.stopPropagation()}>
        {children}
      </Box>
    </Box>
  );
}

export default function AgendaPage({ usuario }: { usuario: Membro }) {
  const hoje = useMemo(() => new Date(), []);
  const [eventos, setEventos] = useState<Agenda[]>([]);
  const [membros, setMembros] = useState<Membro[]>([]);
  const [locais, setLocais] = useState<LocalEvento[]>([]);
  const [cidades, setCidades] = useState<Cidade[]>([]);
  const [selecionado, setSelecionado] = useState<Agenda | null>(null);
  const [editando, setEditando] = useState<Agenda | null>(null);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [mesAtual, setMesAtual] = useState(() => new Date(hoje.getFullYear(), hoje.getMonth(), 1));
  const [erro, setErro] = useState("");

  const musicos = useMemo(() => membros.filter((m) => m.perfil === "MUSICO" && m.ativo), [membros]);

  const eventosPorDia = useMemo(() => {
    const agrupados = new Map<string, Agenda[]>();
    for (const evento of eventos) {
      const chave = chaveData(dataEvento(evento.dataHoraInicio));
      agrupados.set(chave, [...(agrupados.get(chave) ?? []), evento]);
    }

    for (const lista of agrupados.values()) {
      lista.sort((a, b) => dataEvento(a.dataHoraInicio).getTime() - dataEvento(b.dataHoraInicio).getTime());
    }

    return agrupados;
  }, [eventos]);

  const diasCalendario = useMemo(() => {
    const primeiroDia = new Date(mesAtual.getFullYear(), mesAtual.getMonth(), 1);
    const inicio = new Date(primeiroDia);
    inicio.setDate(primeiroDia.getDate() - primeiroDia.getDay());

    return Array.from({ length: 42 }, (_, indice) => {
      const dia = new Date(inicio);
      dia.setDate(inicio.getDate() + indice);
      return dia;
    });
  }, [mesAtual]);

  async function carregar() {
    const [agenda, listaMembros, listaLocais, listaCidades] = await Promise.all([
      api.listarAgenda(),
      api.listarMembros(),
      api.listarLocais(),
      api.listarCidades()
    ]);
    setEventos(agenda);
    setMembros(listaMembros);
    setLocais(listaLocais);
    setCidades(listaCidades);
  }

  useEffect(() => {
    carregar().catch((err) => setErro(err.message));
  }, []);

  async function excluir(evento: Agenda) {
    if (!confirm(`Excluir o evento "${evento.titulo}"?`)) return;
    await api.excluirAgenda(evento.codigoAgenda);
    setSelecionado(null);
    setMostrarForm(false);
    await carregar();
  }

  function podeAlterar(evento: Agenda) {
    return usuario.administrador || evento.criador.codigo === usuario.codigo;
  }

  function abrirFormulario(evento: Agenda | null) {
    setEditando(evento);
    setSelecionado(null);
    setMostrarForm(true);
  }

  function mudarMes(delta: number) {
    setMesAtual((atual) => new Date(atual.getFullYear(), atual.getMonth() + delta, 1));
  }

  function voltarParaHoje() {
    setMesAtual(new Date(hoje.getFullYear(), hoje.getMonth(), 1));
  }

  return (
    <Stack gap="5">
      <Flex justify="space-between" align="center" gap="4" wrap="wrap">
        <Box>
          <Heading size="lg">Agenda</Heading>
          <Text color="fg.muted">Calendário de ensaios, shows e reuniões.</Text>
        </Box>
        <Button onClick={() => abrirFormulario(null)}>Novo evento</Button>
      </Flex>

      {erro && <Text color="red.500">{erro}</Text>}

      <Box bg="bg.panel" borderWidth="1px" borderRadius="lg" overflow="hidden">
        <Flex justify="space-between" align="center" gap="3" p="4" borderBottomWidth="1px" wrap="wrap">
          <HStack>
            <Button size="sm" variant="outline" onClick={() => mudarMes(-1)}>Anterior</Button>
            <Button size="sm" variant="outline" onClick={voltarParaHoje}>Hoje</Button>
            <Button size="sm" variant="outline" onClick={() => mudarMes(1)}>Próximo</Button>
          </HStack>
          <Heading size="md">{nomeMes(mesAtual)}</Heading>
        </Flex>

        <Grid templateColumns="repeat(7, minmax(0, 1fr))" borderBottomWidth="1px">
          {diasSemana.map((dia) => (
            <Box key={dia} px="2" py="2" textAlign="center" fontSize="sm" fontWeight="semibold" color="fg.muted">
              {dia}
            </Box>
          ))}
        </Grid>

        <Grid templateColumns="repeat(7, minmax(0, 1fr))">
          {diasCalendario.map((dia) => {
            const chave = chaveData(dia);
            const eventosDia = eventosPorDia.get(chave) ?? [];
            const foraDoMes = dia.getMonth() !== mesAtual.getMonth();
            const diaAtual = chave === chaveData(hoje);

            return (
              <Box
                key={chave}
                minH={{ base: "104px", md: "132px" }}
                p="2"
                borderRightWidth="1px"
                borderBottomWidth="1px"
                bg={foraDoMes ? "bg.subtle" : "bg.panel"}
                opacity={foraDoMes ? 0.58 : 1}
              >
                <Flex justify="space-between" align="center" mb="2">
                  <Text
                    minW="7"
                    h="7"
                    px="1"
                    display="inline-flex"
                    alignItems="center"
                    justifyContent="center"
                    borderRadius="full"
                    bg={diaAtual ? "blue.500" : "transparent"}
                    color={diaAtual ? "white" : "fg"}
                    fontWeight={diaAtual ? "bold" : "medium"}
                  >
                    {dia.getDate()}
                  </Text>
                </Flex>

                <Stack gap="1">
                  {eventosDia.map((evento) => {
                    const tipo = tiposEvento[evento.tipoEspecifico];
                    return (
                      <Button
                        key={evento.codigoAgenda}
                        size="xs"
                        h="auto"
                        minH="28px"
                        justifyContent="flex-start"
                        variant="ghost"
                        borderLeftWidth="4px"
                        borderLeftColor={tipo.cor}
                        bg="bg.subtle"
                        px="2"
                        py="1"
                        whiteSpace="normal"
                        textAlign="left"
                        lineHeight="1.2"
                        onClick={() => setSelecionado(evento)}
                      >
                        <Box overflow="hidden">
                          <Text fontSize="xs" fontWeight="bold" truncate>{horario(evento.dataHoraInicio)} {evento.titulo}</Text>
                          <Text fontSize="2xs" color="fg.muted" truncate>{tipo.nome}</Text>
                        </Box>
                      </Button>
                    );
                  })}
                </Stack>
              </Box>
            );
          })}
        </Grid>
      </Box>

      {selecionado && (
        <ModalShell onClose={() => setSelecionado(null)}>
          <Box bg="bg.panel" borderWidth="1px" borderRadius="lg" boxShadow="xl" p="5">
            <Stack gap="4">
              <Flex justify="space-between" align="flex-start" gap="4">
                <Box>
                  <Badge style={{ background: tiposEvento[selecionado.tipoEspecifico].cor, color: "white" }}>
                    {tiposEvento[selecionado.tipoEspecifico].nome}
                  </Badge>
                  <Heading size="md" mt="3">{selecionado.titulo}</Heading>
                  <Text color="fg.muted">{dataCompleta(selecionado.dataHoraInicio)} até {dataCompleta(selecionado.dataHoraFim)}</Text>
                </Box>
                <Button size="sm" variant="ghost" onClick={() => setSelecionado(null)}>Fechar</Button>
              </Flex>

              <Stack gap="2">
                <Text>{selecionado.descricao || "Sem descrição."}</Text>
                <Text><b>Local:</b> {selecionado.local.nome}, {selecionado.local.endereco} - {selecionado.local.cidadeNome}/{selecionado.local.uf}</Text>
                <Text><b>Criado por:</b> {selecionado.criador.nome}</Text>
                {selecionado.repertorio && <Text><b>Repertório:</b> {selecionado.repertorio}</Text>}
                {selecionado.observacao && <Text><b>Observação:</b> {selecionado.observacao}</Text>}
                {selecionado.pauta && <Text><b>Pauta:</b> {selecionado.pauta}</Text>}
                {selecionado.cidadeShow && <Text><b>Cidade do show:</b> {selecionado.cidadeShow.nome}/{selecionado.cidadeShow.uf}</Text>}
                {selecionado.musicos.length > 0 && <Text><b>Músicos:</b> {selecionado.musicos.map((m) => m.nome).join(", ")}</Text>}
              </Stack>

              {podeAlterar(selecionado) && (
                <HStack>
                  <Button size="sm" onClick={() => abrirFormulario(selecionado)}>Editar</Button>
                  <Button size="sm" variant="outline" onClick={() => excluir(selecionado)}>Excluir</Button>
                </HStack>
              )}
            </Stack>
          </Box>
        </ModalShell>
      )}

      {mostrarForm && (
        <ModalShell onClose={() => setMostrarForm(false)}>
          <EventForm
            evento={editando}
            locais={locais}
            cidades={cidades}
            musicos={musicos}
            onCancel={() => setMostrarForm(false)}
            onSaved={async () => {
              setMostrarForm(false);
              setEditando(null);
              await carregar();
            }}
          />
        </ModalShell>
      )}
    </Stack>
  );
}
