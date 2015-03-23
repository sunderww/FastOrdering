//
//  CommandMenuViewController.m
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "CommandMenuViewController.h"
#import "CategoryExpandableCell.h"
#import "Dish.h"
#import "DishCell.h"
#import "NSManagedObject+create.h"

#define kDishCellTag(section, row)  ((((section) + 1) * 100) + (row) + 1)
#define kDishCellSectionForTag(tag) (((tag) / 100) - 1)
#define kDishCellRowForTag(tag)     (((tag) % 100) - 1)

@interface CommandMenuViewController ()

@end

@implementation CommandMenuViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  
  NSMutableArray * mutableDishes = [NSMutableArray new];
  categories = self.composition.categories.allObjects;
  
  for (DishCategory * category in categories) {
    [mutableDishes addObject:category.dishes.allObjects];
  }
  dishes = mutableDishes;
}

- (void)didReceiveMemoryWarning {
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}

#pragma mark - SLExpandableTableView delegate and datasource methods

- (BOOL)tableView:(SLExpandableTableView *)tableView canExpandSection:(NSInteger)section {
  return ((DishCategory *)categories[section]).dishes.count > 0;
}

- (BOOL)tableView:(SLExpandableTableView *)tableView needsToDownloadDataForExpandableSection:(NSInteger)section {
  return NO;
}

- (UITableViewCell<UIExpandingTableViewCell> *)tableView:(SLExpandableTableView *)tableView expandingCellForSection:(NSInteger)section {
  static NSString * CellIdentifier = @"CategoryCell";
  
  CategoryExpandableCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
  
  if (!cell) {
    cell = [[CategoryExpandableCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
  }
  
  cell.category = categories[section];
  return cell;
}

- (void)tableView:(SLExpandableTableView *)tableView downloadDataForExpandableSection:(NSInteger)section {
  [tableView expandSection:section animated:YES];
}

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  return categories.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
  return ((NSArray *)dishes[section]).count + 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
  static NSString * CellIdentifier = @"DishCell";
  Dish * dish = dishes[indexPath.section][indexPath.row - 1];
  
  DishCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
  
  if (!cell) {
    cell = [[NSBundle mainBundle] loadNibNamed:@"DishCell" owner:self options:nil][0];
    cell.delegate = self;
  }

  [cell setDish:dish andTag:kDishCellTag(indexPath.section, indexPath.row)];
  return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
  
}


/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
}
*/

@end
