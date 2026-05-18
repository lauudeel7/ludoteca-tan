import { Category } from "../../category/models/category.model";
import { Author } from "../../author/models/author.model";

export class Game {
    id: number | undefined;
    title: string | undefined;
    age: number | undefined;
    category: Category | undefined;
    author: Author | undefined;
}