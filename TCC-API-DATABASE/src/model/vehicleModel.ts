import mongoose from 'mongoose';
import { VehicleType } from '../validators/vehicleValidators';

const vehicleDataSchema = new mongoose.Schema<VehicleType>({
    veiculo: String,
    tipo_combustivel: String,
    distancia_percorrida: Number,
    eficiencia: Number,
    uuid: String
}, { versionKey: false });

export const vehicleDataModel = mongoose.model<VehicleType>('vehicleData', vehicleDataSchema);