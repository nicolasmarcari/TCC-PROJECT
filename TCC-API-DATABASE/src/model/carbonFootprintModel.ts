import mongoose from 'mongoose';
import { CarbonFootprintType } from '../validators/carbonFootprintValidator';

const carbonFootprintDataSchema = new mongoose.Schema<CarbonFootprintType> ({
    pegada_de_carbono: String,
    uuid: String
});

export const carbonFootprintDataModel = mongoose.model<CarbonFootprintType>('carbonFootPrint', carbonFootprintDataSchema);