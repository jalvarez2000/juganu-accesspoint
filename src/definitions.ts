declare module "@capacitor/core" {
  interface PluginRegistry {
    JuganuAccessPoint: JuganuAccessPointPlugin;
  }
}

export interface JuganuAccessPointPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
  connectAP(params: { ssid: string, password: string}): Promise<{value: boolean}>;
}
