import { Category } from "../../category/models/category.model";
import { Author } from "../../author/models/author.model";

export interface Game {
    id: number;
    title: string;
    age: number;
    category: Category;
    author: Author;
}