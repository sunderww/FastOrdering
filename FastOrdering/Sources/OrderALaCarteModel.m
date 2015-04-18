//
//  OrderALaCarteModel.m
//  FastOrdering
//
//  Created by Sunder on 20/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "OrderALaCarteModel.h"
#import "DishCategory+Custom.h"
#import "Dish.h"
#import "CategoryExpandableCell.h"
#import "DishCell.h"

#define kDishCellTag(section, row)  ((((section) + 1) * 100) + (row) + 1)
#define kDishCellSectionForTag(tag) (((tag) / 100) - 1)
#define kDishCellRowForTag(tag)     (((tag) % 100) - 1)

@implementation OrderALaCarteModel

#warning THINGS TO DO
// TODO:
// add dishes without categories at the end

- (instancetype)init
{
  self = [super init];
  if (self) {
    NSMutableArray * tmpDishes = [NSMutableArray new];
    categories = [DishCategory categoriesWithParentId:nil];
    
    for (DishCategory * category in categories) {
      [tmpDishes addObject:category.availableDishes];
    }
    dishes = tmpDishes;
  }
  return self;
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
    //        cell = [[NSBundle mainBundle] loadNibNamed:@"NotificationCell" owner:self options:nil][0];
    cell = [[DishCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
  }

  
  cell.textLabel.text = [NSString stringWithFormat:@"%@ (%@)", dish.name, dish.serverId];
  return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
  Dish * dish = dishes[indexPath.section][indexPath.row - 1];
  [self.delegate dishClicked:dish];
  [tableView deselectRowAtIndexPath:indexPath animated:NO];
}

@end
