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
#import "MenuModel.h"
#import "MenuComposition.h"
#import "NSManagedObject+create.h"
#import "Order+Custom.h"
#import "OrderedDish.h"

#define kDishCellTag(section, row)  ((((section) + 1) * 100) + (row) + 1)
#define kDishCellSectionForTag(tag) (((tag) / 100) - 1)
#define kDishCellRowForTag(tag)     (((tag) % 100) - 1)

@implementation OrderALaCarteModel

- (instancetype)initWithOrder:(Order *)order
{
	self = [super init];
	if (self) {
		self.order = order;		
	}
	return self;
}

- (void)reloadData {
	NSMutableArray * tmpDishes = [NSMutableArray new];
	
	carteMenu = [MenuModel new].alacarte;
	dishes = self.order.alacarteDishes;
	compositions = carteMenu.compositions.allObjects;

	for (MenuComposition * comp in compositions) {
		DishCategory * category = comp.categories.allObjects.firstObject;
		NSMutableArray * categoryDishes = [NSMutableArray new];
		
		for (Dish * dish in category.availableDishes) {
			OrderedDish * ordered = [self.order orderedDishWithDish:dish andComposition:comp];
			
			if (!ordered) {
				ordered = [OrderedDish create];
				ordered.dish = dish;
				ordered.qty = @0;
				ordered.comment = @"";
				ordered.menu = carteMenu;
				ordered.order = self.order;
			}
			[categoryDishes addObject:ordered];
		}
		[tmpDishes addObject:categoryDishes];
	}
	
	dishes = tmpDishes;
}

#pragma mark - SLExpandableTableView delegate and datasource methods

- (BOOL)tableView:(SLExpandableTableView *)tableView canExpandSection:(NSInteger)section {
	DishCategory * category = ((MenuComposition *)compositions[section]).categories.allObjects.firstObject;
	return category.dishes.count > 0;
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
	
	cell.composition = compositions[section];
	return cell;
}

- (void)tableView:(SLExpandableTableView *)tableView downloadDataForExpandableSection:(NSInteger)section {
	[tableView expandSection:section animated:YES];
}

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return compositions.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return ((NSArray *)dishes[section]).count + 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString * CellIdentifier = @"DishCell";

	OrderedDish * dish = dishes[indexPath.section][indexPath.row - 1];
	DishCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (!cell) {
		cell = [[NSBundle mainBundle] loadNibNamed:@"DishCell" owner:self options:nil][0];
		cell.delegate = self;
	}
	
	[cell setDish:dish.dish andTag:kDishCellTag(indexPath.section, indexPath.row - 1)];
	[cell setQuantity:dish.qty.integerValue];
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
	if (!self.editing)
		return ;

	if (!textField.text.length)
		textField.text = @"0";
	
	NSUInteger row = kDishCellRowForTag(textField.tag);
	NSUInteger section = kDishCellSectionForTag(textField.tag);
	
	OrderedDish * ordered = dishes[section][row];
	ordered.qty = @(textField.text.integerValue);
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
	NSString * text = [textField.text stringByReplacingCharactersInRange:range withString:string];
	if (!text.length)
		text = @"0";
	
	NSUInteger row = kDishCellRowForTag(textField.tag);
	NSUInteger section = kDishCellSectionForTag(textField.tag);
	
	OrderedDish * ordered = dishes[section][row];	
	ordered.qty = @(text.integerValue);
	return YES;
}

@end
