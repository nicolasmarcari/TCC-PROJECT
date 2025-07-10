import mongoose from 'mongoose';

class MongoDB {
    private readonly mongoURI: string;

    constructor() {
        if (!process.env.MONGO_URI) {
            console.error('MONGO_URI is not defined in the enviroment.');
            process.exit(1);
        }

        this.mongoURI = process.env.MONGO_URI;
    }

    public async connect(): Promise<void> {
        try {
            await mongoose.connect(this.mongoURI);
            console.log('MongoDB connected!');
        } catch (error) {
            console.error('Failed to connect on mongoDB: ', error);
            process.exit(1);
        }
    }
}

export const database = new MongoDB()