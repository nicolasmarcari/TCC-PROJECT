import { z } from 'zod';

export const vehicleDataSchema = z.object({
    veiculo: z.string(),
    tipo_combustivel: z.string(),
    distancia_percorrida: z.number(),
    eficiencia: z.number(),
    uuid: z.string()
})

export type VehicleType = z.infer<typeof vehicleDataSchema>