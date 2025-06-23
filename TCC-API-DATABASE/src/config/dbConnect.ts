import mongoose from 'mongoose';

export const connectDB = async () => {
    try {
        await mongoose.connect(process.env.MONGO_URI!);
        console.log('MongoDB connected!');
    } catch (err) {
        console.error(`Error to connect on MongoDB. ${err}`);
        process.exit(1);
    }
};

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