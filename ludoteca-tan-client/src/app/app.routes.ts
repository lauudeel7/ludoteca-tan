import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'games',
    pathMatch: 'full'
  },

  {
    path: 'games',
    loadComponent: () =>
      import('./game/game-list/game-list.page').then(m => m.GameListPage)
  },

  {
    path: 'authors',
    loadComponent: () =>
      import('./author/author-list/author-list.page').then(m => m.AuthorListPage)
  },

  {
    path: 'categories',
    loadComponent: () =>
      import('./category/category-list/category-list.page').then(m => m.CategoryListPage)
  },

  {
    path: 'clients',
    loadComponent: () =>
      import('./client/client-list/client-list.page').then(m => m.ClientListPage)
  },
  {
    path: 'loans',
    loadComponent: () =>
      import('./loan/loan-list/loan-list.page').then(m => m.LoanListPage)
  },

  {
    path: 'login',
    loadComponent: () =>
      import('./features/login/pages/login/login').then(m => m.Login)
  },

  {
    path: '**',
    redirectTo: 'games'
  }

];
