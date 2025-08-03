import { Router } from 'express';
import { saveVehicleData } from '../controller/saveVehicleController';
import { validateVehicle } from '../middlewares/vehicleMiddleware';
import { vehicleDataSchema } from '../validators/vehicleValidators';
import { getVehicle } from '../controller/getVehicleData';
import authentication from '../middlewares/authentication';

const router = Router();

router.use(authentication);

router.post('/saveVehicle', validateVehicle(vehicleDataSchema), saveVehicleData);
router.get('/getVehicle/:id', getVehicle);

export default router;
