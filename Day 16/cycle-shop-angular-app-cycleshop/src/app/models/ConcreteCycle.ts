import { Cycle } from "./cycle";

export class ConcreteCycle implements Cycle {

    id: number = 0;

    color: string = '';

    brand: string = '';

    quantity: number = 0;

    numBorrowed: number = 0;
    price: number = 0;
    selectedQuantity?: number = 0;

}