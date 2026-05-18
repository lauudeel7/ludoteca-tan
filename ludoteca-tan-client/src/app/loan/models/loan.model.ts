import { Client } from '../../client/models/client.model';

export interface Loan {
  id?: number;
  game: {
    id: number;
    title?: string;
  };
  client: Client;
  startDate: string;
  endDate: string;
}
