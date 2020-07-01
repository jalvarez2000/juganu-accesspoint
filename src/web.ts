
import { JuganuAccessPointPlugin } from './definitions';
import { WebPlugin} from '@capacitor/core';

export class JuganuAccessPointWeb extends WebPlugin implements JuganuAccessPointPlugin {
  constructor() {
    super({
      name: 'JuganuAccessPoint',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }

  async connectAP(params: { ssid: string, password: string}): Promise<{value: boolean}> {
    console.log('WEB FUNCTIONALITY NOT IMPLEMENTED. PARAMS',params);
    let ret : { value: false};
    return ret;
  }
}

const JuganuAccessPoint = new JuganuAccessPointWeb();

export { JuganuAccessPoint };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(JuganuAccessPoint);
