//
//  OrderReviewModel.m
//  FastOrdering
//
//  Created by Sunder on 08/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "OrderReviewModel.h"
#import "ReviewExpandableCell.h"
#import "OrderContent.h"
#import "MenuComposition.h"
#import "Menu.h"
#import "OrderedDish.h"
#import "Dish.h"
#import "DishCell.h"

#define kDishCellTag(section, row)  ((((section) + 1) * 100) + (row) + 1)
#define kDishCellSectionForTag(tag) (((tag) / 100) - 1)
#define kDishCellRowForTag(tag)     (((tag) % 100) - 1)

@implementation OrderReviewModel

- (void)setOrder:(Order *)order {
	_order = order;
	[self reloadData];
}

- (void)reloadData {
	NSMutableArray * titles = [NSMutableArray new];
	NSMutableArray * contents = [NSMutableArray new];
	
	for (OrderContent * content in self.order.orderContents) {
		[titles addObject:[NSString stringWithFormat:@"%@ - %@", content.menuComposition.menu.name, content.menuComposition.name]];
		[contents addObject:content.dishes.allObjects];
	}
//	if (self.order.dishes.count) {
//		[titles addObject:@"A la carte"];
//		[contents addObject:self.order.dishes.allObjects];
//	}
	
	sections = titles;
	dishes = contents;
	[self.tableView reloadData];
}

#pragma mark - SLExpandableTableView delegate and datasource methods

- (BOOL)tableView:(SLExpandableTableView *)tableView canExpandSection:(NSInteger)section {
	return YES;
}

- (BOOL)tableView:(SLExpandableTableView *)tableView needsToDownloadDataForExpandableSection:(NSInteger)section {
	return NO;
}

- (UITableViewCell<UIExpandingTableViewCell> *)tableView:(SLExpandableTableView *)tableView expandingCellForSection:(NSInteger)section {
	static NSString * CellIdentifier = @"ReviewTitleCell";
	
	ReviewExpandableCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (!cell) {
		//        cell = [[NSBundle mainBundle] loadNibNamed:@"NotificationCell" owner:self options:nil][0];
		cell = [[ReviewExpandableCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
	}
	
	cell.text = sections[section];
	return cell;
}

- (void)tableView:(SLExpandableTableView *)tableView downloadDataForExpandableSection:(NSInteger)section {
	[tableView expandSection:section animated:YES];
}

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return sections.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return ((NSArray *)dishes[section]).count + 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString * CellIdentifier = @"ReviewCell";
	OrderedDish * dish = dishes[indexPath.section][indexPath.row - 1];
	
	DishCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (!cell) {
		cell = [[NSBundle mainBundle] loadNibNamed:@"DishCell" owner:self options:nil][0];
		cell.delegate = self;
		//    cell = [[DishCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
	}
	
	[cell setDish:dish.dish andTag:kDishCellTag(indexPath.section, indexPath.row - 1)];
	[cell setQuantity:dish.quantity.integerValue];
	
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	DishCell * cell = (DishCell *)[self tableView:tableView cellForRowAtIndexPath:indexPath];
	[cell.textField becomeFirstResponder];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark - UIScrollView delegate methods

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
	[responder resignFirstResponder];
}

- (void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView {
	[responder resignFirstResponder];
}
#pragma mark - UITextField delegate

- (void)textFieldDidBeginEditing:(UITextField *)textField {
	textField.text = @"";
	responder = textField;
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
	if (!textField.text.length)
		textField.text = @"0";
	
	NSUInteger row = kDishCellRowForTag(textField.tag);
	NSUInteger section = kDishCellSectionForTag(textField.tag);
	
	OrderedDish * dish = dishes[section][row];
	dish.quantity = @(textField.text.integerValue);
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
	NSString * text = [textField.text stringByReplacingCharactersInRange:range withString:string];
	if (!text.length)
		text = @"0";
	
	NSUInteger row = kDishCellRowForTag(textField.tag);
	NSUInteger section = kDishCellSectionForTag(textField.tag);
	
	OrderedDish * dish = dishes[section][row];
	dish.quantity = @(text.integerValue);

	return YES;
}

@end
