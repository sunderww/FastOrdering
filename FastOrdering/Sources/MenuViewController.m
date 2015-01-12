//
//  MenuViewController.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "MenuViewController.h"
#import "DishCategory+Custom.h"
#import "Dish+Custom.h"
#import "NSManagedObject+create.h"

@interface MenuViewController ()

@end

@implementation MenuViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    if (!self.categoryData || !self.dishData) {
        self.categoryData = [DishCategory categoriesWithParentId:nil];
        self.dishData = [Dish dishesWithParentId:nil];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)goBack {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - UITableView delegate and datasource methods

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section) {
        PPLog(@"DISH SELECTED");
    } else {
        DishCategory * category = self.categoryData[indexPath.row];
        MenuViewController * nextController = [[MenuViewController alloc] initWithNibName:@"MenuView" bundle:nil];

        nextController.categoryData = category.subcategories.allObjects;
        nextController.dishData = category.dishes.allObjects;
        DLog(@"find a way to push this view controller without navigation controller or assign the main view a navigation controller");
        [self.navigationController pushViewController:nextController animated:YES];
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section)
        return self.dishData.count;
    return self.categoryData.count;
}

- (UITableViewCell *)tableView:(UITableView *)_tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * CellIdentifier = @"MenuCell";
    UITableViewCell * cell = [_tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (!cell) {
//        cell = [[NSBundle mainBundle] loadNibNamed:@"MenuCell" owner:self options:nil][0];
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    if (indexPath.section) {
        Dish * dish = self.dishData[indexPath.row];
        cell.textLabel.text = dish.name;
    } else {
        DishCategory * category = self.categoryData[indexPath.row];
        cell.textLabel.text = category.name;
    }
    
    return cell;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
