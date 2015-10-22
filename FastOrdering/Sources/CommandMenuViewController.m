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
#import "OrderedDish.h"
#import "DishCategory+Custom.h"

#define kDishCellTag(section, row)  ((((section) + 1) * 100) + (row) + 1)
#define kDishCellSectionForTag(tag) (((tag) / 100) - 1)
#define kDishCellRowForTag(tag)     (((tag) % 100) - 1)

@interface CommandMenuViewController ()

@end

@implementation CommandMenuViewController

- (void)viewDidLoad {
	[super viewDidLoad];
	
	[orderButton setTitle:NSLocalizedString(@"add", @"").uppercaseString forState:UIControlStateNormal];
	
	NSMutableArray * mutableDishes = [NSMutableArray new];
	counts = [NSMutableArray new];
	categories = self.composition.categories.allObjects;
	
	NSUInteger i = 0;
	for (DishCategory * category in categories) {
		[counts addObject:[NSMutableArray new]];
		for (NSUInteger j = 0 ; j < category.dishes.count ; ++j)
			[counts[i] addObject:@0];
		[mutableDishes addObject:category.availableDishes];
		++i;
	}
	dishes = mutableDishes;
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
	// Dispose of any resources that can be recreated.
}

#pragma mark - Helper methods

- (void)order {
	NSMutableArray * orderedDishes = [NSMutableArray new];

	for (NSUInteger i = 0 ; i < dishes.count ; ++i) {
		for (NSUInteger j = 0 ; j < ((NSArray *)dishes[i]).count ; ++j) {
			NSNumber * quantity = counts[i][j];
			
			if (quantity.unsignedIntegerValue > 0) {
				OrderedDish * dish = [OrderedDish create];
				
				dish.dish = dishes[i][j];
				dish.quantity = quantity;
				dish.menu = self.composition.menu;
				[orderedDishes addObject:dish];
			}
		}
	}
	
	[self.delegate didOrderDishes:orderedDishes];
}

#pragma mark - Actions

- (IBAction)orderTaken {
	[self order];
	[self.delegate popCommandMenuView];
}

- (IBAction)cancel {
	[self.delegate popCommandMenuView];
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

#pragma mark - UIScrollView delegate methods

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
	[responder resignFirstResponder];
}

- (void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView {
	[responder resignFirstResponder];
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
	
	[cell setDish:dish andTag:kDishCellTag(indexPath.section, indexPath.row - 1)];
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	DishCell * cell = (DishCell *)[self tableView:tableView cellForRowAtIndexPath:indexPath];
	[cell.textField becomeFirstResponder];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
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
	counts[section][row] = @(textField.text.integerValue);
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
	NSString * text = [textField.text stringByReplacingCharactersInRange:range withString:string];
	if (!text.length)
		text = @"0";
	
	NSUInteger row = kDishCellRowForTag(textField.tag);
	NSUInteger section = kDishCellSectionForTag(textField.tag);
	counts[section][row] = @(text.integerValue);
	return YES;
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
