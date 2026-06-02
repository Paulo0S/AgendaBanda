import { Button } from "@chakra-ui/react";
import { useTheme } from "next-themes";
import { LuMoon, LuSun } from "react-icons/lu";

export function ColorModeButton() {
  const { theme, setTheme } = useTheme();
  const escuro = theme === "dark";

  return (
    <Button variant="outline" size="sm" onClick={() => setTheme(escuro ? "light" : "dark")}>
      {escuro ? <LuSun /> : <LuMoon />}
    </Button>
  );
}
