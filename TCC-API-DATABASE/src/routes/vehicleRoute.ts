import { Router } from 'express';
import { saveVehicleData } from '../controller/saveVehicleController';
import { validateVehicle } from '../middlewares/vehicleMiddleware';
import { vehicleDataSchema } from '../validators/vehicleValidators';

const router = Router();

router.post('/saveVehicle', validateVehicle(vehicleDataSchema), saveVehicleData);

export default router;
