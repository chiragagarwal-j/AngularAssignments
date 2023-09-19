export class Cycle {
    id: number | undefined;
    brand: string | undefined;
    stock: number | undefined;
    numBorrowed: number | undefined;

    get numAvailable(): number | undefined {
        return this.stock !== undefined && this.numBorrowed !== undefined ? this.stock - this.numBorrowed : undefined;
    }
}
