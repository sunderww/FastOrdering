//
//  OrderMenuModel.m
//  FastOrdering
//
//  Created by Sunder on 20/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "OrderMenuModel.h"
#import "NSManagedObject+create.h"
#import "Menu.h"
#import "MenuComposition.h"
#import "MenuExpandableCell.h"
#import "CommandMenuViewController.h"

@implementation OrderMenuModel

- (instancetype)init
{
  self = [super init];
  if (self) {
    NSMutableArray * compos = [NSMutableArray new];
    menus = [Menu allObjects];

    for (Menu * menu in menus) {
      [compos addObject:menu.compositions.allObjects];
    }
    compositions = compos;
  }
  return self;
}

#pragma mark - SLExpandableTableView delegate and datasource methods

- (BOOL)tableView:(SLExpandableTableView *)tableView canExpandSection:(NSInteger)section {
  return ((Menu *)menus[section]).compositions.count > 0;
}

- (BOOL)tableView:(SLExpandableTableView *)tableView needsToDownloadDataForExpandableSection:(NSInteger)section {
  return NO;
}

- (UITableViewCell<UIExpandingTableViewCell> *)tableView:(SLExpandableTableView *)tableView expandingCellForSection:(NSInteger)section {
  static NSString * CellIdentifier = @"MenuCell";
  
  MenuExpandableCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
  
  if (!cell) {
    //        cell = [[NSBundle mainBundle] loadNibNamed:@"NotificationCell" owner:self options:nil][0];
    cell = [[MenuExpandableCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
  }
  
  cell.menu = menus[section];
  return cell;
}

- (void)tableView:(SLExpandableTableView *)tableView downloadDataForExpandableSection:(NSInteger)section {
  [tableView expandSection:section animated:YES];
}

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  return menus.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
  return ((NSArray *)compositions[section]).count + 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
  static NSString * CellIdentifier = @"CompositionCell";
  MenuComposition * composition = compositions[indexPath.section][indexPath.row - 1];

  UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
  
  if (!cell) {
    //        cell = [[NSBundle mainBundle] loadNibNamed:@"NotificationCell" owner:self options:nil][0];
    cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
  }
  
  cell.textLabel.text = [NSString stringWithFormat:@"%@ (%@)", composition.name, composition.serverId];
  return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
  MenuComposition * composition = compositions[indexPath.section][indexPath.row - 1];
  [self.delegate menuCompositionClicked:composition];
}


@end
