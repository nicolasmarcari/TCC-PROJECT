import { z } from 'zod';

export const carbonFootprintDataSchema = z.object({
    pegada_de_carbono: z.number(),
    uuid: z.string()
});

export type CarbonFootprintType = z.infer<typeof carbonFootprintDataSchema>;
